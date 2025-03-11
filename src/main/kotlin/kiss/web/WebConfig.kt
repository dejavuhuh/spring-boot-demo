package kiss.web

import kiss.ratelimit.RateLimiterInterceptor
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class WebConfig(val redissonClient: RedissonClient) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(RateLimiterInterceptor(redissonClient))
    }
}