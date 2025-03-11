package kiss.security

import kiss.BaseEntity
import org.babyfish.jimmer.sql.Entity

@Entity
interface Permission : BaseEntity {

    val name: String
}