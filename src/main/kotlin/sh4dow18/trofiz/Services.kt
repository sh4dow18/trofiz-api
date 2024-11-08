package sh4dow18.trofiz
// Services Requirements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
// Platform Service Interface where the functions to be used in
// Spring Abstract Platform Service are declared
interface PlatformService {
    fun findAll(): List<PlatformResponse>
    fun insert(platformRequest: PlatformRequest): PlatformResponse
}
// Spring Abstract Platform Service
@Service
class AbstractPlatformService(
    // Platform Service Props
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val platformMapper: PlatformMapper
): PlatformService {
    override fun findAll(): List<PlatformResponse> {
        // Returns all Platforms as a Platform Responses List
        return platformMapper.platformsListToPlatformResponsesList(platformRepository.findAll())
    }
    // @Transactional is a Tag that establishes that is a Transactional Service Function. This
    // one makes a transaction when this service function is in operation.
    @Transactional(rollbackFor = [ElementAlreadyExists::class])
    override fun insert(platformRequest: PlatformRequest): PlatformResponse {
        // Transforms Name in Platform Request in lowercase and replace spaces with "-"
        // Example: "Play Station 5" -> "play-station-5"
        val platformId = getIdByName(platformRequest.name)
        // Verifies if the platform already exists
        if (platformRepository.findById(platformId).orElse(null) != null) {
            throw ElementAlreadyExists(platformRequest.name, "Plataforma" )
        }
        // If not exists, create the new platform
        val newPlatform = platformMapper.platformRequestToPlatform(platformRequest)
        // Save new Platform and Returns it as Platform Response
        return platformMapper.platformToPlatformResponse(platformRepository.save(newPlatform))
    }
}
// Genre Service Interface where the functions to be used in
// Spring Abstract Genre Service are declared
interface GenreService {
    fun findAll(): List<GenreResponse>
    fun insert(genreRequest: GenreRequest): GenreResponse
}
// Spring Abstract Genre Service
@Service
class AbstractGenreService(
    // Genre Service Props
    @Autowired
    val genreRepository: GenreRepository,
    @Autowired
    val genreMapper: GenreMapper
): GenreService {
    override fun findAll(): List<GenreResponse> {
        // Returns all Genres as a Genre Responses List
        return genreMapper.genresListToGenreResponsesList(genreRepository.findAll())
    }
    override fun insert(genreRequest: GenreRequest): GenreResponse {
        // Transforms Name in Genre Request in lowercase and replace spaces with "-"
        // Example: "Interactive Adventure" -> "interactive-adventure"
        val genreId = getIdByName(genreRequest.name)
        // Verifies if the genre already exists
        if (genreRepository.findById(genreId).orElse(null) != null) {
            throw ElementAlreadyExists(genreRequest.name, "Género" )
        }
        // If not exists, create the new genre
        val newGenre = genreMapper.genreRequestToGenre(genreRequest)
        // Transforms the New Genre to Genre Response and Returns It
        return genreMapper.genreToGenreResponse(genreRepository.save(newGenre))
    }
}
// Game Service Interface where the functions to be used in
// Spring Abstract Game Service are declared
interface GameService {
    fun findAll(): List<GameResponse>
    fun insert(gameRequest: GameRequest): GameResponse
}
// Spring Abstract Game Service
@Service
class AbstractGameService(
    // Game Service Props
    @Autowired
    val gameRepository: GameRepository,
    @Autowired
    val gameMapper: GameMapper,
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val genreRepository: GenreRepository
): GameService {
    override fun findAll(): List<GameResponse> {
        // Transforms a Games List to a Game Responses List
        return gameMapper.gamesListToGameResponsesList(gameRepository.findAll())
    }
    override fun insert(gameRequest: GameRequest): GameResponse {
        // Verifies if the game already exists
        if (gameRepository.findById(getIdByName(gameRequest.name)).orElse(null) != null) {
            throw ElementAlreadyExists(gameRequest.name, "Juego")
        }
        // If not exists, create the new game
        val newGame = gameMapper.gameRequestToGame(gameRequest)
        // Add existing platforms to the new game
        newGame.platformsList = connectEntities(platformRepository, gameRequest.platformsList)
        // Add existing genres to the new game
        newGame.genresList = connectEntities(genreRepository, gameRequest.genresList)
        // Transforms the New Game to a Game Response and Returns it
        return gameMapper.gameToGameResponse(gameRepository.save(newGame))
    }
}