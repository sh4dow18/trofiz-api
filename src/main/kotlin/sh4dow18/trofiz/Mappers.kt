package sh4dow18.trofiz
// Mappers Requirements
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
// Mappers Constants
const val UTILS_PATH = "sh4dow18.trofiz.UtilsKt"
const val EMPTY_LIST = "java(java.util.Collections.emptyList())"
const val EMPTY_SET = "java(java.util.Collections.emptySet())"
// Platform Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PlatformMapper {
    // Get the Platform id using its own name
    @Mapping(target = "id", expression = "java($UTILS_PATH.getIdByName(platformRequest.getName()))")
    // Set each list and each set as empty
    @Mapping(target = "gameLogsList", expression = EMPTY_LIST)
    @Mapping(target = "gamesList", expression = EMPTY_SET)
    fun platformRequestToPlatform(
        platformRequest: PlatformRequest
    ): Platform
    fun platformToPlatformResponse(
        platform: Platform
    ): PlatformResponse
    fun platformsListToPlatformResponsesList(
        platformsList: List<Platform>
    ): List<PlatformResponse>
}
// Genre Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface GenreMapper {
    // Get the Genre id using its own name
    @Mapping(target = "id", expression = "java($UTILS_PATH.getIdByName(genreRequest.getName()))")
    // Set games list as empty
    @Mapping(target = "gamesList", expression = EMPTY_SET)
    fun genreRequestToGenre(
        genreRequest: GenreRequest
    ): Genre
    fun genreToGenreResponse(
        genre: Genre
    ): GenreResponse
    fun genresListToGenreResponsesList(
        genresList: List<Genre>
    ): List<GenreResponse>
}
// Game Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface GameMapper {
    // Get Game slug using its own name
    @Mapping(target = "id", expression = "java($UTILS_PATH.getIdByName(gameRequest.getName()))")
    // Set each list and each set as empty
    @Mapping(target = "platformsList", expression = EMPTY_SET)
    @Mapping(target = "genresList", expression = EMPTY_SET)
    @Mapping(target = "gameLogsList", expression = EMPTY_LIST)
    fun gameRequestToGame(
        gameRequest: GameRequest
    ): Game
    fun gameToGameResponse(
        game: Game
    ): GameResponse
    fun gamesListToGameResponsesList(
        gamesList: List<Game>
    ): List<GameResponse>
}