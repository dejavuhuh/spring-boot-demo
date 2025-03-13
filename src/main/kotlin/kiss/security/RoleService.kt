package kiss.security

import kiss.security.dto.RoleInput
import kiss.security.dto.RoleSpecification
import org.babyfish.jimmer.client.FetchBy
import org.babyfish.jimmer.sql.exception.SaveException
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
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
    fun list(@ModelAttribute specification: RoleSpecification): List<@FetchBy("ROLE_LIST_VIEW") Role> {
        return sql.executeQuery(Role::class) {
            where(specification)
            select(table.fetch(ROLE_LIST_VIEW))
        }
    }

    companion object {
        val ROLE_LIST_VIEW = newFetcher(Role::class).by {
            allScalarFields()
            inheritedRoles {
                name()
            }
        }
    }
}
