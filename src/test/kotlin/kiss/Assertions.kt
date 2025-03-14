package kiss

import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.assertj.MockMvcTester

fun MockMvcTester.MockMvcRequestBuilder.hasStatus(status: HttpStatus) = exchange().assertThat().hasStatus(status)

// fun MockMvcTester.MockMvcRequestBuilder.body(body: Any) = exchange().assertThat().hasStatus(status)
