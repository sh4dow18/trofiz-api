package sh4dow18.trofiz
// Log Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
// Log Test Main Class
@SpringBootTest
class LogTests(
    // Log Tests Props
    @Autowired
    val logRepository: LogRepository,
    @Autowired
    val logMapper: LogMapper,
    @Autowired
    val actionTypeRepository: ActionTypeRepository,
    @Autowired
    val userRepository: UserRepository
) {
    @Test
    // Makes it transactional to use Action Type and User in Log
    @Transactional
    fun findAll() {
        // Transforms the Logs List to a Logs Responses List
        logMapper.logsListToLogsResponsesList(logRepository.findAll())
    }
    @Test
    fun insert() {
        // Insert Log Test Prop
        val logRequest = LogRequest("Registro de Juego: Days Gone", "inserción", 1)
        // Check if the Action Type submitted already exists
        val actionType = actionTypeRepository.findById(logRequest.actionTypeId).orElseThrow {
            NoSuchElementExists(logRequest.actionTypeId, "Tipo de Acción")
        }
        // Check if the user submitted already exists
        val user = userRepository.findById(logRequest.userId).orElseThrow {
            NoSuchElementExists("${logRequest.userId}", "Usuario")
        }
        // If the action type and user exists, create a new log
        val newLog = logMapper.logRequestToLog(logRequest, actionType, user)
        // Transforms the New Log to a Log Response
        logMapper.logToLogResponse(newLog)
    }
}