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
        val privilegesList = mutableSetOf<Privilege>()
        roleRequest.privilegesList.forEach {
            val privilege = privilegeRepository.findById(it).orElseThrow {
                NoSuchElementExists(it, "Privilegio")
            }
            privilegesList.add(privilege)
        }
        // If each privileges exist, create the new role
        val newRole = roleMapper.roleRequestToRole(roleRequest, privilegesList)
        // Transforms the New Role to Role Response
        roleMapper.roleToRoleResponse(newRole)
    }
}