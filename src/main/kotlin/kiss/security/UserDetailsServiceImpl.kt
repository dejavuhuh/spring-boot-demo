package kiss.security

import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailsServiceImpl(val sql: KSqlClient) : UserDetailsService {

    val fetcher = newFetcher(Account::class).by {
        password()
        roles {
            name()
            permissions {
                name()
            }
        }
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val account = sql.createQuery(Account::class) {
            where(
                or(
                    table.email eq username,
                    table.phone eq username
                )
            )
            select(table.fetch(fetcher))
        }.fetchOneOrNull() ?: throw UsernameNotFoundException(username)

        val roles = account.roles.map { it.name }
        val permissions = account.roles.flatMap { it.permissions }.map { it.name }

        return User
            .withUsername(username)
            .password(account.password)
            .roles(*roles.toTypedArray())
            .authorities(*permissions.toTypedArray())
            .build()
    }
}