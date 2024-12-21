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
    val logMapper: LogMapper
) {
    @Test
    // Makes it transactional to use Action Type and User in Log
    @Transactional
    fun findAll() {
        // Transforms the Logs List to a Logs Responses List
        logMapper.logsListToLogsResponsesList(logRepository.findAll())
    }
}