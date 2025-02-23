package kiss

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class SpringBootDemoApplicationTests {

    fun contextLoads() {
    }

}
