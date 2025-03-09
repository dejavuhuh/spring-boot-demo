package kiss.sms

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service

@ConfigurationProperties(prefix = "sms")
data class SmsProperties(
    val accessKeyId: String,
    val accessKeySecret: String,
    val signName: String
)

@Service
@EnableConfigurationProperties(SmsProperties::class)
class SmsService(
    val properties: SmsProperties,
    val objectMapper: ObjectMapper,
) {

//    val client by lazy {
//        val config = Config()
//            .setAccessKeyId(properties.accessKeyId)
//            .setAccessKeySecret(properties.accessKeySecret)
//        config.endpoint = "dysmsapi.cn-shenzhen.aliyuncs.com"
//        Client(config)
//    }

    fun send(
        to: String,
        smsTemplateId: String,
        contextObject: Map<String, Any>
    ) {
//        val request = SendSmsRequest()
//            .setPhoneNumbers(to)
//            .setTemplateCode(smsTemplateId)
//            .setSignName(properties.signName)
//            .setTemplateParam(objectMapper.writeValueAsString(contextObject))
//        client.sendSms(request)
    }
}