package sh4dow18.trofiz
// Mappers Requirements
import org.mapstruct.Context
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
// Privilege Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PrivilegeMapper {
    // Get Privilege id using its own name
    @Mapping(target = "id", expression = "java($UTILS_PATH.getIdByName(privilegeRequest.getName()))")
    // Set enabled always as true
    @Mapping(target = "enabled", expression = "java(true)")
    // Set each set as empty
    @Mapping(target = "rolesList", expression = EMPTY_SET)
    fun privilegeRequestToPrivilege(
        privilegeRequest: PrivilegeRequest
    ): Privilege
    fun privilegeToPrivilegeResponse(
        privilege: Privilege
    ): PrivilegeResponse
    fun privilegesListToPrivilegeResponsesList(
        privilegesList: List<Privilege>
    ): List<PrivilegeResponse>
}
// Role Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface RoleMapper {
    // Set each set as empty
    @Mapping(target = "privilegesList", expression = EMPTY_SET)
    // Set each list as empty
    @Mapping(target = "usersList", expression = EMPTY_LIST)
    fun roleRequestToRole(
        roleRequest: RoleRequest,
    ): Role
    fun roleToRoleResponse(
        role: Role
    ): RoleResponse
    fun rolesListToRoleResponsesList(
        rolesList: List<Role>
    ): List<RoleResponse>
}
// User Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    // Mapping the variables not submitted
    @Mapping(target = "createdDate", expression = "java($UTILS_PATH.getCurrentDate())")
    @Mapping(target = "enabled", expression = "java(true)")
    @Mapping(target = "image", expression = "java(false)")
    @Mapping(target = "role", expression = "java(existingRole)")
    // Set each list as empty
    @Mapping(target = "gameLogsList", expression = EMPTY_LIST)
    @Mapping(target = "logsList", expression = EMPTY_LIST)
    fun userRequestToUser(
        userRequest: UserRequest,
        @Context existingRole: Role
    ): User
    @Mapping(target = "createdDate", expression = "java($UTILS_PATH.getDateAsString(user.getCreatedDate()))")
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    fun userToUserResponse(
        user: User
    ): UserResponse
    fun usersListToUserResponsesList(
        usersList: List<User>
    ): List<UserResponse>
}