package kiss.verification

import kiss.mail.MailService
import kiss.ratelimit.Limit
import kiss.ratelimit.RateLimit
import kiss.sms.SmsService
import kiss.sms.SmsTemplateId
import kiss.utils.RandomUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/verification-code")
class VerificationCodeService(
    val storage: VerificationCodeStorage,
    val mailService: MailService,
    val smsService: SmsService,
) {

    @PostMapping("/send")
    @RateLimit(
        Limit(capacity = 15, duration = 1, timeUnit = TimeUnit.HOURS),
        Limit(capacity = 60, duration = 1, timeUnit = TimeUnit.DAYS),
    )
    fun send(@RequestBody request: VerificationCodeIdentifier) {
        // 生成并存储验证码
        val verificationCode = RandomUtils.number(6)
        storage.store(request, verificationCode)

        // 发送验证码
        when (request.receiverType) {
            ReceiverType.EMAIL -> sendByEmail(request.receiver, verificationCode)
            ReceiverType.PHONE -> sendByPhone(request.receiver, verificationCode)
        }
    }

    private fun sendByEmail(email: String, verificationCode: String) {
        mailService.send(
            to = email,
            subject = "验证码",
            htmlTemplateId = "verification-code",
            contextObject = mapOf("verificationCode" to verificationCode)
        )
    }

    private fun sendByPhone(phone: String, verificationCode: String) {
        smsService.send(
            to = phone,
            smsTemplateId = SmsTemplateId.VERIFICATION_CODE,
            contextObject = mapOf("verificationCode" to verificationCode)
        )
    }
}