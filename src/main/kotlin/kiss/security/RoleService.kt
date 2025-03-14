package kiss.security

import kiss.security.dto.RoleInput
import kiss.security.dto.RoleSpecification
import org.babyfish.jimmer.client.FetchBy
import org.babyfish.jimmer.sql.exception.SaveException
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(RoleService.URI)
class RoleService(val sql: KSqlClient) {

    @PostMapping
    @Throws(
        RoleException.RoleNameAlreadyExists::class,
        RoleException.RoleCodeAlreadyExists::class,
    )
    fun createRole(@RequestBody input: RoleInput) {
        try {
            sql.insert(input)
        } catch (ex: SaveException.NotUnique) {
            when {
                ex.isMatched(RoleProps.NAME) -> throw RoleException.roleNameAlreadyExists(input.name)
                ex.isMatched(RoleProps.CODE) -> throw RoleException.roleCodeAlreadyExists(input.code)
                else -> throw ex
            }
        }
    }

    @DeleteMapping("/{id}")
    fun deleteRole(@PathVariable id: Int) {
        sql.deleteById(Role::class, id)
    }

    @PutMapping("/{id}")
    @Throws(
        RoleException.RoleNameAlreadyExists::class,
        RoleException.RoleCodeAlreadyExists::class,
    )
    fun updateRole(@PathVariable id: Int, @RequestBody input: RoleInput) {
        try {
            sql.update(input.toEntity { this.id = id })
        } catch (ex: SaveException.NotUnique) {
            when {
                ex.isMatched(RoleProps.NAME) -> throw RoleException.roleNameAlreadyExists(input.name)
                ex.isMatched(RoleProps.CODE) -> throw RoleException.roleCodeAlreadyExists(input.code)
                else -> throw ex
            }
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
        const val URI = "/roles"
        val ROLE_LIST_VIEW = newFetcher(Role::class).by {
            allScalarFields()
            inheritedRoles {
                name()
            }
        }
    }
}
