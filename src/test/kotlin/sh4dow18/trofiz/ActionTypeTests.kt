package sh4dow18.trofiz
// Action Type Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

// Action Type Test Main Class
@SpringBootTest
class ActionTypeTests(
    // Action Type Tests Props
    @Autowired
    val actionTypeRepository: ActionTypeRepository,
    @Autowired
    val actionTypeMapper: ActionTypeMapper
) {
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun findAll() {
        // Transforms the Action Types List to a Action Types Responses List
        actionTypeMapper.actionTypesListToActionTypeResponsesList(actionTypeRepository.findAll())
    }
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun insert() {
        // Insert Action Type Test Prop
        val actionTypeRequest = ActionTypeRequest("eliminar")
        // Check if the Action Type submitted already exists
        val id = getIdByName(actionTypeRequest.name)
        if (actionTypeRepository.findById(id).orElse(null) != null) {
            throw ElementAlreadyExists(id, "Tipo de Acción")
        }
        // If the Action Type exists, create a new Action Type
        val newActionType = actionTypeMapper.actionTypeRequestToActionType(actionTypeRequest)
        // Transforms the New Action Type to a Action Type Response
        actionTypeMapper.actionTypeToActionTypeResponse(newActionType)
    }
}