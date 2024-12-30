package sh4dow18.trofiz
// Game Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
// Game Tests Main Class
@SpringBootTest
class GameTests(
    // Game Tests Props
    @Autowired
    val gameRepository: GameRepository,
    @Autowired
    val gameMapper: GameMapper,
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val genreRepository: GenreRepository,
    @Autowired
    val reviewMapper: ReviewMapper
) {
    @Test
    // Makes it transactional to use Platforms in Game and to use User Repository in User Validation
    @Transactional
    fun findAll() {
        // Transforms a Games List to a Game Responses List
        gameMapper.gamesListToGameResponsesList(gameRepository.findAll())
    }
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun findTop10ByNameContainingIgnoreCase() {
        // Find Top 10 By Name Test Prop
        val name = "Juego 1"
        // Transforms the first 10 Games from a Games List to a Game Responses List
        gameMapper.gamesListToGameResponsesList(gameRepository.findTop10ByNameContainingIgnoreCase(name))
    }
    @Test
    // Makes it transactional to use Review in Game and to use User Repository in User Validation
    @Transactional
    fun findAllReviewsById() {
        // Find All Reviews by id Props
        val id = "days-gone"
        // Check if the game already exists
        val game = gameRepository.findById(id).orElseThrow {
            NoSuchElementExists(id, "Juego")
        }
        // Transforms a Reviews List to a Review Responses List
        reviewMapper.reviewsListToReviewResponsesList(game.reviewsList)
    }
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun findById() {
        // Find by id Props
        val id = "resident-evil"
        // Check if the game already exists
        val game = gameRepository.findById(id).orElseThrow {
            NoSuchElementExists(id, "Juego")
        }
        // Transforms a Game to a Game Response
        gameMapper.gameToGameResponse(game)
    }
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun insert() {
        // Insert Genre Test Props
        val platformsSet: Set<String> = setOf("playstation-5")
        val genresSet: Set<String> = setOf("aventura")
        val gameRequest = GameRequest("Juego de Prueba: Con Puntos / Y Slashes", 4.5f, 97,
            "2024-11-01", "http://image.com", platformsSet, genresSet)
        // Verifies if the game already exists
        if (gameRepository.findById(getIdByName(gameRequest.name)).orElse(null) != null) {
            throw ElementAlreadyExists(gameRequest.name, "Juego")
        }
        // Check if each platform submitted exists
        val platformsList = platformRepository.findAllById(gameRequest.platformsList)
        if (platformsList.size != gameRequest.platformsList.size) {
            val missingIds = gameRequest.platformsList - platformsList.map { it.id }.toSet()
            throw NoSuchElementsExists(missingIds.toList(), "Plataformas")
        }
        // Check if each genre submitted exists
        val genresList = genreRepository.findAllById(gameRequest.genresList)
        if (genresList.size != gameRequest.genresList.size) {
            val missingIds = gameRequest.genresList - genresList.map { it.id }.toSet()
            throw NoSuchElementsExists(missingIds.toList(), "Géneros")
        }
        // If the game not exists and each platform and genre exists, create the new game
        val newGame = gameMapper.gameRequestToGame(gameRequest, platformsList.toSet(), genresList.toSet())
        // Transforms the New Game to a Game Response and Returns it
        gameMapper.gameToGameResponse(newGame)
    }
}