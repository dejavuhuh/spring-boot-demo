package kiss.ratelimit

import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy
import io.github.bucket4j.distributed.serialization.Mapper
import io.github.bucket4j.redis.redisson.Bucket4jRedisson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kiss.utils.clientIp
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.springframework.http.HttpStatus
import org.springframework.web.ErrorResponseException
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import java.time.Duration.ofDays

class RateLimiterInterceptor(redissonClient: RedissonClient) : HandlerInterceptor {

    private val proxyManager = Bucket4jRedisson
        .casBasedBuilder((redissonClient as Redisson).commandExecutor)
        .expirationAfterWrite(
            ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(ofDays(1))
        )
        .keyMapper(Mapper.STRING)
        .build()

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        val rateLimit = handler.getMethodAnnotation(RateLimit::class.java) ?: return true
        check(rateLimit.limits.isNotEmpty()) { "At least one limit must be defined" }

        val bucketKey = "${request.requestURI}:${request.clientIp}"
        val bucket = proxyManager.getProxy(bucketKey) {
            val builder = BucketConfiguration.builder()
            for (limit in rateLimit.limits) {
                val period = Duration.of(limit.duration, limit.timeUnit.toChronoUnit())
                builder.addLimit { it.capacity(limit.capacity).refillGreedy(limit.capacity, period) }
            }
            builder.build()
        }

        if (bucket.tryConsume(1)) {
            return true
        }

        throw ErrorResponseException(HttpStatus.TOO_MANY_REQUESTS)
    }
}
