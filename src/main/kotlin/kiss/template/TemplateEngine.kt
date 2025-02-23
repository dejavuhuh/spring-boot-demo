package kiss.template

import kiss.utils.ClassPathResourceLoader
import org.springframework.stereotype.Service
import kotlin.reflect.full.memberProperties

@Service
class TemplateEngine {

    final inline fun <reified T : Any> render(
        templateLocation: String,
        contextObject: T
    ): String {
        val resourceLocation = getResourceLocation(templateLocation)
        val template = ClassPathResourceLoader.getResourceText(resourceLocation)

        var result = template
        for (property in T::class.memberProperties) {
            val name = property.name
            val value = property.get(contextObject)
            result = result.replace($$"${$${name}}", value.toString())
        }
        return result
    }

    fun getResourceLocation(templateLocation: String): String {
        return "templates/${templateLocation}"
    }
}