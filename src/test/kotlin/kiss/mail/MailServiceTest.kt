package kiss.mail

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetup
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import kiss.template.TemplateEngine
import kiss.utils.ClassPathResourceLoader
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.test.util.TestSocketUtils

@ExtendWith(MockKExtension::class)
class MailServiceTest {

    companion object {
        private const val USERNAME = "kiss"
        private const val PASSWORD = "pwd"
        private val PORT = TestSocketUtils.findAvailableTcpPort()

        val mailSender = JavaMailSenderImpl().apply {
            host = "localhost"
            port = PORT
            username = USERNAME
            password = PASSWORD
        }

        @JvmStatic
        @RegisterExtension
        val mailServer: GreenMailExtension = GreenMailExtension(ServerSetup(PORT, null, ServerSetup.PROTOCOL_SMTP))
            .withConfiguration(GreenMailConfiguration.aConfig().withUser(USERNAME, PASSWORD))
            .withPerMethodLifecycle(false)
    }

    @Test
    fun `Send mail`() {
        val templateId = "test"
        val templateEngine = TemplateEngine()
        val mailService = MailService(templateEngine, mailSender)

        val templateLocation = mailService.getTemplateLocation(templateId)
        val resourceLocation = templateEngine.getResourceLocation(templateLocation)

        data class Context(val var1: String, val var2: Int)

        val context = Context("value2", 1)
        val expectedContent = "${context.var1}, ${context.var2}"
        val expectedTo = "example@gmail.com"
        val expectedSubject = "Test"

        mockkObject(ClassPathResourceLoader)
        every { ClassPathResourceLoader.getResourceText(resourceLocation) } returns expectedContent

        mailService.send(
            to = expectedTo,
            subject = expectedSubject,
            htmlTemplateId = templateId,
            contextObject = mapOf(
                "var1" to context.var1,
                "var2" to context.var2
            )
        )

        val receivedMessages = mailServer.receivedMessages
        receivedMessages.size.shouldBe(1)
        val receivedMessage = receivedMessages[0]

        // to
        val recipients = receivedMessage.allRecipients
        recipients.size.shouldBe(1)
        recipients[0].toString().shouldBe(expectedTo)

        // subject
        receivedMessage.subject.shouldBe(expectedSubject)

        // content
        GreenMailUtil.getBody(receivedMessage).shouldBe(expectedContent)
    }
}