package kiss.security

import kiss.security.dto.RoleInput
import kiss.security.dto.RoleSpecification
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.exception.SaveException
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/roles")
class RoleService(val sql: KSqlClient) {

    @PostMapping
    @Throws(RoleException.RoleAlreadyExists::class)
    fun createRole(@RequestBody input: RoleInput) {
        try {
            sql.insert(input)
        } catch (_: SaveException.NotUnique) {
            throw RoleException.roleAlreadyExists(input.name)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteRole(@PathVariable id: Int) {
        sql.deleteById(Role::class, id)
    }

    @PutMapping("/{id}")
    @Throws(RoleException.RoleAlreadyExists::class)
    fun updateRole(@PathVariable id: Int, @RequestBody input: RoleInput) {
        try {
            sql.update(input.toEntity { this.id = id })
        } catch (_: SaveException.NotUnique) {
            throw RoleException.roleAlreadyExists(input.name)
        }
    }

    @GetMapping
    fun list(@ModelAttribute specification: RoleSpecification): Page<Role> {
        TODO()
    }
}
