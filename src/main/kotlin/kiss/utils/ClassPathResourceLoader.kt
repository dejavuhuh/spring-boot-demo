package kiss.utils

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.Reader

object ClassPathResourceLoader {

    private val resourceTextCache: Cache<String, String> =
        Caffeine
            .newBuilder()
            .maximumSize(100)
            .build()

    fun getResourceText(location: String): String {
        return resourceTextCache.get(location) {
            val inputStream =
                ClassPathResourceLoader::class.java.classLoader.getResourceAsStream(location) as? BufferedInputStream
                    ?: throw IllegalArgumentException("Resource not found: $location")

            (BufferedReader(inputStream.reader())).use(Reader::readText)
        }
    }
}