package kiss.mail

import kiss.template.TemplateEngine
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailService(
    val templateEngine: TemplateEngine,
    val mailSender: JavaMailSender,
) {

    fun send(
        to: String,
        subject: String,
        htmlTemplateId: String,
        contextObject: Map<String, Any>
    ) {
        val templateLocation = getTemplateLocation(htmlTemplateId)
        val content = templateEngine.render(templateLocation, contextObject)
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(content, true)
        mailSender.send(message)
    }

    fun getTemplateLocation(htmlTemplateId: String): String {
        return "mail/${htmlTemplateId}.html"
    }
}
