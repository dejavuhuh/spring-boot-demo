package kiss

import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.Instant

@MappedSuperclass
interface BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int
    
    val createdAt: Instant
}
