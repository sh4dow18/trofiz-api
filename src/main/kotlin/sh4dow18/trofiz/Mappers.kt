package sh4dow18.trofiz
// Mappers Requirements
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
const val UTILS_PATH = "sh4dow18.trofiz.UtilsKt"
// Platform Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PlatformMapper {
    // Get the Platform id using its own name
    @Mapping(target = "id", expression = "java($UTILS_PATH.getPlatformId(platformRequest.getName()))")
    // Set each list and each set as empty
    @Mapping(target = "gameLogsList", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "gamesList", expression = "java(java.util.Collections.emptySet())")
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
    @Mapping(target = "id", expression = "java($UTILS_PATH.getPlatformId(genreRequest.getName()))")
    // Set games list as empty
    @Mapping(target = "gamesList", expression = "java(java.util.Collections.emptySet())")
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
    @Mapping(target = "id", expression = "java($UTILS_PATH.getPlatformId(gameRequest.getName()))")
    // Set each list and each set as empty
    @Mapping(target = "platformsList", expression = "java(java.util.Collections.emptySet())")
    @Mapping(target = "genresList", expression = "java(java.util.Collections.emptySet())")
    @Mapping(target = "gameLogsList", expression = "java(java.util.Collections.emptyList())")
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