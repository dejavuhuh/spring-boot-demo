package kiss.ratelimit

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
annotation class RateLimit(vararg val limits: Limit)


annotation class Limit(val capacity: Long, val duration: Long, val timeUnit: TimeUnit)