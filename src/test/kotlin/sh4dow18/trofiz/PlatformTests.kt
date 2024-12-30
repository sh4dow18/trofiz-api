package sh4dow18.trofiz
// Platform Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
// Platform Test Main Class
@SpringBootTest
class PlatformTests(
    // Platform Tests Props
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val platformMapper: PlatformMapper
) {
    @Test
    @Transactional
    fun findAll() {
        // Transforms a Platforms List to a Platform Responses List
        platformMapper.platformsListToPlatformResponsesList(platformRepository.findAll())
    }
    @Test
    @Transactional
    fun insert() {
        // Insert Platform Test Prop
        val platformRequest = PlatformRequest("Plataforma de Prueba")
        // Transforms Name in Platform Request in lowercase and replace spaces with "-"
        // Example: "Play Station 5" -> "play-station-5"
        val platformId = getIdByName(platformRequest.name)
        // Verifies if the platform already exists
        if (platformRepository.findById(platformId).orElse(null) != null) {
            throw ElementAlreadyExists(platformRequest.name, "Plataforma")
        }
        // If not exists, create the new platform
        val newPlatform = platformMapper.platformRequestToPlatform(platformRequest)
        // Save new Platform and Returns it as Platform Response
        platformMapper.platformToPlatformResponse(newPlatform)
    }
}