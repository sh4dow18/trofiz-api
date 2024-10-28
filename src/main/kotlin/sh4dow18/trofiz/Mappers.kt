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
    fun platformRequestToPlatform(
        platformRequest: PlatformRequest
    ): Platform
    // Get the Platform name using its own id
    @Mapping(target = "name", expression = "java($UTILS_PATH.getPlatformName(platform.getId()))")
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
    fun genreRequestToGenre(
        genreRequest: GenreRequest
    ): Genre
    // Get the Genre name using its own id
    @Mapping(target = "name", expression = "java($UTILS_PATH.getPlatformName(genre.getId()))")
    fun genreToGenreResponse(
        genre: Genre
    ): GenreResponse
    fun genresListToGenreResponsesList(
        genresList: List<Genre>
    ): List<GenreResponse>
}