package kiss.security

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import kiss.TestcontainersConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.HttpCookie

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.sql.init.mode=always"
    ]
)
@Import(TestcontainersConfiguration::class)
class AuthServiceTest @Autowired constructor(val template: TestRestTemplate) {

    @MockkBean
    lateinit var userDetailsService: UserDetailsService

    @Test
    fun `Should return 200 status code after sign in`(@Autowired passwordEncoder: PasswordEncoder) {
        template.postForEntity<Unit>("/protected").statusCode.shouldBe(HttpStatus.UNAUTHORIZED)

        // Sign in
        every { userDetailsService.loadUserByUsername("admin") } returns User
            .withUsername("admin")
            .password(passwordEncoder.encode("pw"))
            .build()

        val response = template.postForEntity<Unit>("/auth/sign-in", SignInRequest("admin", "pw"))
        response.statusCode.shouldBe(HttpStatus.OK)
        val setCookie = response.headers["Set-Cookie"].shouldNotBeNull()[0]
        val cookie = HttpCookie.parse(setCookie)[0]

        val headers = HttpHeaders()
        headers.add("Cookie", "${cookie.name}=${cookie.value}")
        val request = HttpEntity<Unit>(headers)
        template.postForEntity<Unit>("/protected", request).statusCode.shouldBe(HttpStatus.OK)
    }
}

@RestController
class ProtectedController {

    @PostMapping("/protected")
    fun protected() {
    }
}
