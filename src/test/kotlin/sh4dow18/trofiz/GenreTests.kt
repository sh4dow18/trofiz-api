package sh4dow18.trofiz
// Genre Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
// Genre Test Main Class
@SpringBootTest
class GenreTests(
    // Genre Tests Props
    @Autowired
    val genreRepository: GenreRepository,
    @Autowired
    val genreMapper: GenreMapper
) {
    @Test
    fun findAll() {
        // Transforms a Genres List to a Genres Responses List
        genreMapper.genresListToGenreResponsesList(genreRepository.findAll())
    }
    @Test
    fun insert() {
        // Insert Genre Test Prop
        val genreRequest = GenreRequest("Género de Prueba")
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