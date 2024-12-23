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
        val roleRequest = RoleRequest("administrator", listOf("add-game"), 1)
        // Verifies if the Role already exists
        if (roleRepository.findByNameIgnoringCase(roleRequest.name).orElse(null) != null) {
            throw ElementAlreadyExists(roleRequest.name, "Rol")
        }
        // Check if the privileges on the submitted role already exist
        val privilegesList = privilegeRepository.findAllById(roleRequest.privilegesList)
        if (privilegesList.size != roleRequest.privilegesList.size) {
            val missingIds = roleRequest.privilegesList - privilegesList.map { it.id }.toSet()
            throw NoSuchElementsExists(missingIds.toList(), "Privilegios")
        }
        // If each privileges exist, create the new role
        val newRole = roleMapper.roleRequestToRole(roleRequest, privilegesList.toSet())
        // Transforms the New Role to Role Response
        roleMapper.roleToRoleResponse(newRole)
    }
    @Test
    fun update() {
        // Update Role Test Prop
        val updateRoleRequest = UpdateRoleRequest(1, listOf("add-game"), 1)
        // Verifies if the Role already exists
        val role = roleRepository.findById(updateRoleRequest.id).orElseThrow {
            NoSuchElementExists("${updateRoleRequest.id}", "Rol")
        }
        // Check if the privileges on the submitted role already exist
        val privilegesList = privilegeRepository.findAllById(updateRoleRequest.privilegesList)
        if (privilegesList.size != updateRoleRequest.privilegesList.size) {
            val missingIds = updateRoleRequest.privilegesList - privilegesList.map { it.id }.toSet()
            throw NoSuchElementsExists(missingIds.toList(), "Privilegios")
        }
        // If the Role exists and the Privileges Exists, update it
        role.privilegesList = privilegesList.toSet()
        // Transforms the New Role to Role Response
        roleMapper.roleToRoleResponse(role)
    }
}