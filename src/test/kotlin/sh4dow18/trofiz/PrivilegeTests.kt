package sh4dow18.trofiz
// Privilege Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

// Privilege Test Main Class
@SpringBootTest
class PrivilegeTests(
    // Privilege Tests Props
    @Autowired
    val privilegeRepository: PrivilegeRepository,
    @Autowired
    val privilegeMapper: PrivilegeMapper,
    @Autowired
    val userRepository: UserRepository,
) {
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun findAll() {
        // Find All Test Prop
        val userId = 1L
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, userId, "ver-privilegios")
        // Transforms a Privilege List to a Privilege Responses List
        privilegeMapper.privilegesListToPrivilegeResponsesList(privilegeRepository.findAll())
    }
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun insert() {
        // Insert Privilege Test Prop
        val privilegeRequest = PrivilegeRequest("Add Games", "El Usuario puede agregar juegos", 1)
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, privilegeRequest.userId, "agregar-privilegios")
        // Transforms Name in Privilege Request in lowercase and replace spaces with "-"
        // Example: "Add Games" -> "add-games"
        val privilegeId = getIdByName(privilegeRequest.name)
        // Verifies if the Privilege already exists
        if (privilegeRepository.findById(privilegeId).orElse(null) != null) {
            throw ElementAlreadyExists(privilegeId, "Privilegio")
        }
        // If not exists, create the new Privilege
        val newPrivilege = privilegeMapper.privilegeRequestToPrivilege(privilegeRequest)
        // Transforms the New Privilege to Privilege Response
        privilegeMapper.privilegeToPrivilegeResponse(newPrivilege)
    }
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun update() {
        // Update Privilege Status Test Prop
        val updatePrivilegeRequest = UpdatePrivilegeRequest("add-game", 1)
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, updatePrivilegeRequest.userId, "actualizar-privilegios")
        // Verifies if the Privilege already exists
        val privilege = privilegeRepository.findById(updatePrivilegeRequest.id).orElseThrow {
            NoSuchElementExists(updatePrivilegeRequest.id,"Privilegio")
        }
        // Change enabled from true to false and vice versa
        privilege.enabled = !privilege.enabled
        // Transforms the Privilege to Privilege Response
        privilegeMapper.privilegeToPrivilegeResponse(privilege)
    }
}