package kiss

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<SpringBootDemoApplication>().with(TestcontainersConfiguration::class).run(*args)
}
