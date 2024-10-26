package sh4dow18.trofiz
// Mappers Requirements
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
// Platform Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PlatformMapper {
    // Create a game logs list as an empty list, when creating a "Platform"
    @Mapping(target = "gamesLogsList", expression = "java(java.util.Collections.emptyList())")
    fun platformRequestToPlatform(
        platformRequest: PlatformRequest
    ): Platform
    fun platformToPlatformResponse(
        platform: Platform
    ): PlatformResponse
}