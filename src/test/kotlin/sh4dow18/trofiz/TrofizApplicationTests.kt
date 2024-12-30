package sh4dow18.trofiz
// Main Trofiz Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
// Main Trofiz Tests Class
@SpringBootTest
// Insert all Needed Information
@Sql("/insert-needed.sql")
class TrofizApplicationTests {
	@Test
	fun contextLoads() {
	}
}