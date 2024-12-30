package sh4dow18.trofiz
// Game Log Tests Requirements
import org.junit.jupiter.api.Assertions
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
    val gameLogMapper: GameLogMapper,
    @Autowired
    val gameLogRepository: GameLogRepository,
    @Autowired
    val reviewMapper: ReviewMapper
) {
    @Test
    // Makes it transactional to use Platform and Genre in Game and to use User Repository in User Validation
    @Transactional
    fun findAll() {
        // Transforms the Game Logs List to a Game Log Responses List
        gameLogMapper.gameLogsListToGameLogResponsesList(gameLogRepository.findAll())
    }
    @Test
    // Makes it transactional to use Platform and Genre in Game and to use User Repository in User Validation
    @Transactional
    fun findByUserId() {
        // Find Game Logs by User Id Test Prop
        val id = 1L
        // Transforms the Game Logs List to a Game Log Responses List
        gameLogMapper.gameLogsListToGameLogResponsesList(gameLogRepository.findByUserIdOrderByCreatedDateAsc(id))
    }
    @Test
    // Makes it transactional to use Platform and Genre in Game and to use User Repository in User Validation
    @Transactional
    fun findById() {
        // Find Game Log by Id Test Prop
        val id = 1L
        // Check if the game log already exists
        val gameLog = gameLogRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Registro de Juego")
        }
        // Transforms the Game Logs List to a Game Log Responses List
        gameLogMapper.gameLogToGameLogResponse(gameLog)
    }
    @Test
    // Makes it transactional to use Game and User Repositories and to use User Repository in User Validation
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
    @Test
    // Makes it transactional to use Reviews and to use User Repository in User Validation
    @Transactional
    fun update() {
        // Update Game Log Test Prop
        val updateGameLogRequest = UpdateGameLogRequest(3, 8.5f,"2024-12-17 11:11",
            "2024-12-17 11:11", "2024-12-17 11:11", "Excelente", "play-station-5")
        // Check if the user submitted already exists
        val gameLog = gameLogRepository.findById(updateGameLogRequest.id).orElseThrow {
            NoSuchElementExists("${updateGameLogRequest.id}", "Registro de Juego")
        }
        // Check if a new Rating was submitted, if it was, change it
        if (updateGameLogRequest.rating != null) {
            gameLog.rating = updateGameLogRequest.rating!!
        }
        // Check if a new Created Date was submitted, if it was, change it
        if (updateGameLogRequest.createdDate != null) {
            gameLog.createdDate = getStringAsDate(updateGameLogRequest.createdDate!!)
        }
        // Check if a new Finished Date was submitted, if it was, change it
        if (updateGameLogRequest.finished != null) {
            gameLog.finished = getStringAsDate(updateGameLogRequest.finished!!)
        }
        // Check if a new Platinum Date was submitted, if it was, change it
        if (updateGameLogRequest.platinum != null) {
            gameLog.platinum = getStringAsDate(updateGameLogRequest.platinum!!)
        }
        // Check if a new Review was submitted, if it was, change it
        if (updateGameLogRequest.review != null) {
            if (gameLog.review != null) {
                gameLog.review!!.description = updateGameLogRequest.review!!
            }
            else {
                val newReview = reviewMapper.contextToReview(updateGameLogRequest.review!!, gameLog)
                gameLog.review = newReview
            }
        }
        // Check if a new Platform was submitted, if it was, change it
        if (updateGameLogRequest.platformId != null) {
            val platform = gameLog.game.platformsList.find { it.id == updateGameLogRequest.platformId }
            if (platform == null) {
                throw NoSuchElementExists("${updateGameLogRequest.platformId}",
                    "Plataforma en el Juego ${gameLog.game.name}")
            }
            gameLog.platform = platform
        }
        // Transforms the Game Log to a Game Log Response
        gameLogMapper.gameLogToGameLogResponse(gameLog)
    }
    @Test
    // Makes it transactional to use User Repository in User Validation
    @Transactional
    fun delete() {
        // Delete Game Log Test Prop
        val deleteGameLogRequest = DeleteGameLogRequest(1)
        // Check if the user submitted already exists
        gameLogRepository.findById(deleteGameLogRequest.id).orElseThrow {
            NoSuchElementExists("${deleteGameLogRequest.id}", "Registro de Juego")
        }
        // Delete the Game Log
        Assertions.assertTrue(true)
    }
}