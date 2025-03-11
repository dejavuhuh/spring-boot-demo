package kiss.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class ClassPathResourceLoaderTest {

    @Test
    fun `Get resource text`() {
        ClassPathResourceLoader
            .getResourceText("templates/mail/test.html")
            .shouldBe(
                $$"""
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta content="width=device-width, initial-scale=1" name="viewport">
                    <title>Document</title>
                </head>
                <body>
                <h1>${var1}</h1>
                <p>${var2}</p>
                </body>
                </html>
            """.trimIndent()
            )
    }

    @Test
    fun `Should throw exception when resource not found`() {
        val notFoundResourceLocation = "templates/mail/not-found.html"

        shouldThrow<IllegalArgumentException> {
            ClassPathResourceLoader.getResourceText(notFoundResourceLocation)
        }.message.shouldContain(notFoundResourceLocation)
    }
}