package kiss.template

import kiss.utils.ClassPathResourceLoader
import org.springframework.stereotype.Service

@Service
class TemplateEngine {

    fun render(
        templateLocation: String,
        contextObject: Map<String, Any>
    ): String {
        val resourceLocation = getResourceLocation(templateLocation)
        val template = ClassPathResourceLoader.getResourceText(resourceLocation)

        var result = template
        for ((key, value) in contextObject) {
            result = result.replace($$"${$${key}}", value.toString())
        }
        return result
    }

    fun getResourceLocation(templateLocation: String): String {
        return "templates/${templateLocation}"
    }
}
