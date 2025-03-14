package kiss.security

import kiss.TestcontainersConfiguration
import kiss.hasStatus
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.assertj.MockMvcTester

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(TestcontainersConfiguration::class)
class RoleServiceTest @Autowired constructor(val mvc: MockMvcTester) {

    @Test
    @DisplayName("CRUD")
    fun crud() {
        mvc
            .post()
            .uri(RoleService.URI)
            .content(ByteArray(0))
            .hasStatus(HttpStatus.CREATED)
    }
}
