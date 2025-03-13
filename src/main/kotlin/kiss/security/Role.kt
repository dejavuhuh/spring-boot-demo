package kiss.security

import kiss.BaseEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany

@Entity
interface Role : BaseEntity {

    @Key(group = "uk_name")
    val name: String

    @Key(group = "uk_code")
    val code: String

    @ManyToMany
    val inheritedRoles: List<Role>

    @ManyToMany
    val permissions: List<Permission>
}
