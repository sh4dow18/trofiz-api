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
    val genreRepository: GenreRepository
) {
    @Test
    fun findAll() {
        // Transforms a Games List to a Game Responses List
        gameMapper.gamesListToGameResponsesList(gameRepository.findAll())
    }
    @Test
    fun findTop10ByNameContainingIgnoreCase() {
        val name = "Juego 1"
        // Transforms the first 10 Games from a Games List to a Game Responses List
        gameMapper.gamesListToGameResponsesList(gameRepository.findTop10ByNameContainingIgnoreCase(name))
    }
    @Test
    fun findById() {
        // Find by id Props
        val id = "resident-evil"
        val platformsSet: Set<PlatformRequest> = setOf(PlatformRequest("Plataforma 1"),
            PlatformRequest("Plataforma 2"))
        val genresSet: Set<GenreRequest> = setOf(GenreRequest("Género 1"), GenreRequest("Género 2"))
        val gameRequest = GameRequest("Juego de Prueba: Con Puntos / Y Slashes", 4.5f, 97,
            "2024-11-01", "http://image.com", platformsSet, genresSet
        )
        // Transforms a Game to a Game Response
        gameMapper.gameToGameResponse(gameRepository.findById(id).orElse(gameMapper.gameRequestToGame(gameRequest)))
    }
    @Test
    fun insert() {
        // Insert Genre Test Props
        val platformsSet: Set<String> = setOf("play-station-5")
        val genresSet: Set<String> = setOf("aventura")
        val gameRequest = GameRequest("Juego de Prueba: Con Puntos / Y Slashes", 4.5f, 97,
            "2024-11-01", "http://image.com", platformsSet, genresSet
            )
        // Verifies if the game already exists
        if (gameRepository.findById(getIdByName(gameRequest.name)).orElse(null) != null) {
            throw ElementAlreadyExists(gameRequest.name, "Juego")
        }
        // Check if each platform submitted exists
        val platformsList = platformRepository.findAllById(gameRequest.platformsList)
        if (platformsList.size != gameRequest.platformsList.size) {
            val missingIds = gameRequest.platformsList - platformsList.map { it.id }.toSet()
            throw NoSuchElementExists(missingIds.toString(), "Plataformas")
        }
        // Check if each genre submitted exists
        val genresList = genreRepository.findAllById(gameRequest.genresList)
        if (genresList.size != gameRequest.genresList.size) {
            val missingIds = gameRequest.genresList - genresList.map { it.id }.toSet()
            throw NoSuchElementExists(missingIds.toString(), "Géneros")
        }
        // If the game not exists and each platform and genre exists, create the new game
        // If not exists, create the new game
        val newGame = gameMapper.gameRequestToGame(gameRequest)
        // Add existing platforms to the new game
        newGame.platformsList = platformsList.toSet()
        // Add existing genres to the new game
        newGame.genresList = genresList.toSet()
        // Transforms the New Game to a Game Response
        gameMapper.gameToGameResponse(newGame)
    }
}