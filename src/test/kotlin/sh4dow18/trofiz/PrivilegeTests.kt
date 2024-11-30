package sh4dow18.trofiz
// Privilege Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
// Privilege Test Main Class
@SpringBootTest
class PrivilegeTests(
    // Privilege Tests Props
    @Autowired
    val privilegeRepository: PrivilegeRepository,
    @Autowired
    val privilegeMapper: PrivilegeMapper
) {
    @Test
    fun findAll() {
        // Transforms a Privilege List to a Privilege Responses List
        privilegeMapper.privilegesListToPrivilegeResponsesList(privilegeRepository.findAll())
    }
    @Test
    fun insert() {
        // Insert Privilege Test Prop
        val privilegeDetails = PrivilegeRequest("Add Games", "El Usuario puede agregar juegos")
        // Transforms Name in Privilege Request in lowercase and replace spaces with "-"
        // Example: "Add Games" -> "add_games"
        val privilegeId = getIdByName(privilegeDetails.name)
        // Verifies if the Privilege already exists
        if (privilegeRepository.findById(privilegeId).orElse(null) != null) {
            throw ElementAlreadyExists(privilegeId, "Privilegio")
        }
        // If not exists, create the new Privilege
        val newPrivilege = privilegeMapper.privilegeRequestToPrivilege(privilegeDetails)
        // Transforms the New Privilege to Privilege Response
        privilegeMapper.privilegeToPrivilegeResponse(newPrivilege)
    }
}