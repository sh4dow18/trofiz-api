package sh4dow18.trofiz
// Role Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
// Role Test Main Class
@SpringBootTest
class RoleTests(
    // Role Tests Props
    @Autowired
    val roleRepository: RoleRepository,
    @Autowired
    val privilegeRepository: PrivilegeRepository,
    @Autowired
    val roleMapper: RoleMapper
) {
    @Test
    @Transactional
    fun findAll() {
        // Transforms a Role List to a Role Responses List
        roleMapper.rolesListToRoleResponsesList(roleRepository.findAll())
    }
    @Test
    // Makes it transactional to use Privilege Repository
    @Transactional
    fun insert() {
        // Insert Role Test Prop
        val roleRequest = RoleRequest("administrator", listOf("add-game"))
        // Verifies if the Role already exists
        if (roleRepository.findByNameIgnoringCase(roleRequest.name).orElse(null) != null) {
            throw ElementAlreadyExists(roleRequest.name, "Rol")
        }
        // Check if the privileges on the submitted role already exist
        val privilegesList = privilegeRepository.findAllById(roleRequest.privilegesList)
        if (privilegesList.size != roleRequest.privilegesList.size) {
            val missingIds = roleRequest.privilegesList - privilegesList.map { it.id }.toSet()
            throw NoSuchElementExists(missingIds.toString(), "Privilegios")
        }
        // If each privileges exist, create the new role
        val newRole = roleMapper.roleRequestToRole(roleRequest)
        newRole.privilegesList = privilegesList.toSet()
        // Transforms the New Role to Role Response
        roleMapper.roleToRoleResponse(newRole)
    }
}