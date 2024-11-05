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
    fun insert() {
        // Insert Genre Test Props
        val platformsSet: Set<PlatformRequest> = setOf(PlatformRequest("Plataforma 1"),
            PlatformRequest("Plataforma 2"))
        val genresSet: Set<GenreRequest> = setOf(GenreRequest("Género 1"), GenreRequest("Género 2"))
        val gameRequest = GameRequest("Juego de Prueba: Con Puntos / Y Slashes", 4.5f, 97,
            "2024-11-01", "http://image.com", platformsSet, genresSet
            )
        // Verifies if the game already exists
        if (gameRepository.findById(getIdByName(gameRequest.name)).orElse(null) != null) {
            throw ElementAlreadyExists(gameRequest.name, "Juego")
        }
        // If not exists, create the new game
        val newGame = gameMapper.gameRequestToGame(gameRequest)
        // Add existing platforms to the new game
        newGame.platformsList = platformRepository.findAllById(gameRequest.platformsList.map {
            platform -> getIdByName(platform.name)
        }).toSet()
        // Add existing genres to the new game
        newGame.genresList = genreRepository.findAllById(gameRequest.genresList.map {
            genre -> getIdByName(genre.name)
        }).toSet()
        // Transforms the New Game to a Game Response
        gameMapper.gameToGameResponse(newGame)
    }
}