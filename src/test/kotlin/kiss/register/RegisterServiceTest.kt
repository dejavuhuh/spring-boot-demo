package kiss.register

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RegisterServiceTest {

    @Test
    fun `Send verification code`() {
//        val redisTemplate = mockk<StringRedisTemplate>()
//        val mailService = mockk<MailService>()
//        val signUpService = SignUpService(redisTemplate, mailService)
//
//        val email = "example@gmail.com"
//        val verificationCode = "123456"
//        mockkObject(RandomUtils)
//        every { RandomUtils.number(6) } returns verificationCode
//
//        signUpService.sendVerificationCode(
//            SendVerificationCodeRequest(AccountType.EMAIL, email)
//        )
//
//        verify { redisTemplate.opsForValue().set(eq("s"), eq("a")) }
    }
}