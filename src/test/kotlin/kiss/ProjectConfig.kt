package kiss

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringAutowireConstructorExtension

class ProjectConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> {
        return super.extensions() + listOf<Extension>(
            SpringAutowireConstructorExtension
        )
    }
}