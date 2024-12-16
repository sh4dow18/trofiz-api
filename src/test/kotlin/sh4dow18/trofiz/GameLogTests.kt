package sh4dow18.trofiz
// Game Log Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
// Game Log Test Main Class
@SpringBootTest
class GameLogTests(
    // Game Log Tests Props
    @Autowired
    val gameRepository: GameRepository,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val gameLogMapper: GameLogMapper
) {
    @Test
    // Makes it transactional to use Game and User Repositories
    @Transactional
    fun insert() {
        // Insert Game Log Test Prop
        val gameLogRequest = GameLogRequest("days-gone",  1, "play-station-5")
        // Check if the user submitted already exists
        val user = userRepository.findById(gameLogRequest.userId).orElseThrow {
            NoSuchElementExists("${gameLogRequest.userId}", "Usuario")
        }
        // Check if the user already have the game as a game log
        if (user.gameLogsList.any { it.game.id == gameLogRequest.gameId }) {
            throw ElementAlreadyExists(gameLogRequest.gameId, "Registro de Juego con ${user.name}")
        }
        // Check if the game submitted already exists
        val game = gameRepository.findById(gameLogRequest.gameId).orElseThrow {
            NoSuchElementExists(gameLogRequest.gameId, "Juego")
        }
        // Check if the platform submitted already exists in the game submitted
        val platform = game.platformsList.find { it.id == gameLogRequest.platformId }
        if (platform == null) {
            throw NoSuchElementExists(gameLogRequest.platformId, "Plataforma en el Juego ${game.name}")
        }
        // If the game, the user and platform exists, create a new game log
        val newGameLog = gameLogMapper.gameLogRequestToGameLog(gameLogRequest, game, user, platform)
        // Transforms the New Game Log to a Game Log Response
        gameLogMapper.gameLogToGameLogResponse(newGameLog)
    }
}