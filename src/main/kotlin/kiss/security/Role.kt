package kiss.security

import kiss.BaseEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToMany

@Entity
interface Role : BaseEntity {

    val name: String

    @ManyToMany
    val children: List<Role>

    @ManyToMany
    val permissions: List<Permission>
}