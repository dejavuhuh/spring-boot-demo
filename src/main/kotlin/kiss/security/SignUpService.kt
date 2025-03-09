package kiss.security

import kiss.BusinessException
import kiss.mail.MailService
import kiss.sms.SmsService
import kiss.sms.SmsTemplateId
import kiss.utils.RandomUtils
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/sign-up")
class SignUpService(
    val redisTemplate: StringRedisTemplate,
    val mailService: MailService,
    val smsService: SmsService,
) {

    val storeKeyPrefix = "verification-code"

    data class SendVerificationCodeRequest(val accountType: AccountType, val account: String)

    data class SignUpRequest(
        val accountType: AccountType,
        val account: String,
        val verificationCode: String,
        val password: String,
    )

    class InvalidVerificationCodeException : BusinessException()
    class FaultyVerificationCodeException(faultyCode: String) : BusinessException(faultyCode)

    @PostMapping("/verification-code/send")
    fun sendVerificationCode(@RequestBody request: SendVerificationCodeRequest) {
        val (accountType, account) = request

        // 生成并存储验证码
        val verificationCode = RandomUtils.number(6)
        val key = "$storeKeyPrefix:$accountType:$account"
        storeVerificationCode(key, verificationCode)

        // 发送验证码
        when (accountType) {
            AccountType.EMAIL -> sendVerificationCodeByEmail(account, verificationCode)
            AccountType.PHONE -> sendVerificationCodeByPhone(account, verificationCode)
        }
    }

    @PostMapping
    fun signUp(@RequestBody request: SignUpRequest) {
        val (accountType, account, verificationCode) = request

        val key = "$storeKeyPrefix:$accountType:$account"
        val storedVerificationCode = getStoredVerificationCode(key) ?: throw InvalidVerificationCodeException()

        if (verificationCode != storedVerificationCode) {
            throw FaultyVerificationCodeException(verificationCode)
        }
    }

    private fun storeVerificationCode(key: String, verificationCode: String) {
        redisTemplate.opsForValue().set(key, verificationCode, Duration.ofMinutes(5))
    }

    private fun getStoredVerificationCode(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    private fun sendVerificationCodeByEmail(email: String, verificationCode: String) {
        mailService.send(
            to = email,
            subject = "验证码",
            htmlTemplateId = "verification-code",
            contextObject = mapOf("verificationCode" to verificationCode)
        )
    }

    private fun sendVerificationCodeByPhone(phone: String, verificationCode: String) {
        smsService.send(
            to = phone,
            smsTemplateId = SmsTemplateId.VERIFICATION_CODE,
            contextObject = mapOf("verificationCode" to verificationCode)
        )
    }
}