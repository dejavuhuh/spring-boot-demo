package kiss.verification

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class VerificationCodeStorage(redissonClient: RedissonClient) {

    private val maxAttempts = 3
    private val expiredMinutes = 5L
    private val codeCache = redissonClient.getMapCache<VerificationCodeIdentifier, String>("verification-code")
    private val attemptCache = redissonClient.getMapCache<VerificationCodeIdentifier, Int>("verification-code-attempt")

    fun store(key: VerificationCodeIdentifier, value: String) {
        codeCache.fastPut(key, value, expiredMinutes, TimeUnit.MINUTES)
        attemptCache.fastPut(key, 0, expiredMinutes, TimeUnit.MINUTES)
    }

    @Throws(VerificationCodeException::class)
    fun verify(key: VerificationCodeIdentifier, value: String, block: () -> Unit) {
        val stored = codeCache[key] ?: throw VerificationCodeException.expired()
        if (stored != value) {
            // 防止暴力破解
            val attempts = attemptCache.compute(key) { _, v -> v!! + 1 } ?: error("Impossible")
            if (attempts >= maxAttempts) {
                codeCache.remove(key)
                attemptCache.remove(key)
                throw VerificationCodeException.expired()
            }

            throw VerificationCodeException.mismatch()
        }

        block()
        codeCache.remove(key)
        attemptCache.remove(key)
    }
}