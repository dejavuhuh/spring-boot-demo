package kiss.ratelimit

import io.mockk.junit5.MockKExtension
import kiss.TestcontainersConfiguration
import kiss.hasStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.assertj.MockMvcTester
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@SpringBootTest
@ExtendWith(MockKExtension::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestcontainersConfiguration::class)
class RateLimitTest @Autowired constructor(val mvc: MockMvcTester) {

    @Test
    fun `With @RateLimit`() {
        mvc.post().uri("/rate-limit").hasStatus(HttpStatus.OK)
        mvc.post().uri("/rate-limit").hasStatus(HttpStatus.OK)
        mvc.post().uri("/rate-limit").hasStatus(HttpStatus.OK)
        mvc.post().uri("/rate-limit").hasStatus(HttpStatus.TOO_MANY_REQUESTS)
        TimeUnit.MILLISECONDS.sleep(500)
        mvc.post().uri("/rate-limit").hasStatus(HttpStatus.OK)
        mvc.post().uri("/rate-limit").hasStatus(HttpStatus.OK)
        mvc.post().uri("/rate-limit").hasStatus(HttpStatus.OK)
        mvc.post().uri("/rate-limit").hasStatus(HttpStatus.TOO_MANY_REQUESTS)
    }

    @Test
    fun `With empty @RateLimit`() {
        mvc.post().uri("/with-empty-rate-limit").hasStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `Without @RateLimit`() {
        mvc.post().uri("/without-rate-limit").hasStatus(HttpStatus.OK)
    }
}

@RestController
class RateLimitController {

    @RateLimit(
        Limit(capacity = 3, duration = 500, timeUnit = TimeUnit.MILLISECONDS)
    )
    @PostMapping("/rate-limit")
    fun withRateLimit() {
    }

    @PostMapping("/without-rate-limit")
    fun withoutRateLimit() {
    }

    @RateLimit
    @PostMapping("/with-empty-rate-limit")
    fun withEmptyRateLimit() {
    }
}
