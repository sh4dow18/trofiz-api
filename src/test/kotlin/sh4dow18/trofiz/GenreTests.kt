package sh4dow18.trofiz
// Genre Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

// Genre Test Main Class
@SpringBootTest
class GenreTests(
    // Genre Tests Props
    @Autowired
    val genreRepository: GenreRepository,
    @Autowired
    val genreMapper: GenreMapper,
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
        checkUserValidation(userRepository, userId, "ver-géneros")
        // Transforms a Genres List to a Genres Responses List
        genreMapper.genresListToGenreResponsesList(genreRepository.findAll())
    }
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun insert() {
        // Insert Genre Test Prop
        val genreRequest = GenreRequest("Género de Prueba", 1)
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, genreRequest.userId, "agregar-géneros")
        // Transforms Name in Genre Request in lowercase and replace spaces with "-"
        // Example: "Interactive Adventure" -> "interactive-adventure"
        val genreId = getIdByName(genreRequest.name)
        // Verifies if the genre already exists
        if (genreRepository.findById(genreId).orElse(null) != null) {
            throw ElementAlreadyExists(genreRequest.name, "Género" )
        }
        // If not exists, create the new genre
        val newGenre = genreMapper.genreRequestToGenre(genreRequest)
        // Transforms the New Genre to Genre Response
        genreMapper.genreToGenreResponse(newGenre)
    }
}