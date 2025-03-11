package kiss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SpringBootDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringBootDemoApplication>(*args)
}

@RestController
class HelloController {
    @GetMapping("/hello")
    fun hello(): String {
        Thread.sleep(1000000)
        return "Hello, World!"
    }
}