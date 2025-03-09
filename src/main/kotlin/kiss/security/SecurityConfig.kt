package kiss.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            authorizeHttpRequests {
//                authorize(anyRequest, authenticated)
                authorize(anyRequest, permitAll)
            }
            formLogin {}
        }
        return http.build()
    }

    @Bean
    fun userDetailsManager() = InMemoryUserDetailsManager(
        User.withDefaultPasswordEncoder().username("user").password("pw").roles("USER").build(),
        User.withDefaultPasswordEncoder().username("admin").password("pw").roles("USER", "ADMIN").build()
    )
}