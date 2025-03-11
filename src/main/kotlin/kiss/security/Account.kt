package kiss.security

import kiss.BaseEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany

@Entity
interface Account : BaseEntity {

    @Key(group = "uk_phone")
    val phone: String?

    @Key(group = "uk_email")
    val email: String?

    val password: String?

    @ManyToMany
    val roles: List<Role>
}