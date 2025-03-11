package kiss.template

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import kiss.utils.ClassPathResourceLoader
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TemplateEngineTest {

    @Test
    fun `Render template`() {
        val templateEngine = TemplateEngine()
        val templateLocation = "verification-code.html"
        val resourceLocation = templateEngine.getResourceLocation(templateLocation)

        mockkObject(ClassPathResourceLoader)
        every { ClassPathResourceLoader.getResourceText(resourceLocation) } returns $$"${var1}, ${var2}"


        data class Context(val var1: String, val var2: Int)

        val context = Context("value1", 2)
        templateEngine
            .render(templateLocation, context)
            .shouldBe("${context.var1}, ${context.var2}")
    }
}
