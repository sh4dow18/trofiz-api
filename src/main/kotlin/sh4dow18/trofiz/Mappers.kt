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
const val MAP = "stream().map"
const val TO_SET = "collect(java.util.stream.Collectors.toSet())"
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
    // Set each list and each set
    @Mapping(target = "platformsList", expression = "java(platformsList)")
    @Mapping(target = "genresList", expression = "java(genresList)")
    @Mapping(target = "gameLogsList", expression = EMPTY_LIST)
    @Mapping(target = "reviewsList", expression = EMPTY_LIST)
    fun gameRequestToGame(
        gameRequest: GameRequest,
        @Context platformsList: Set<Platform>,
        @Context genresList: Set<Genre>,
    ): Game
    @Mapping(target = "platformsList", expression = "java(game.getPlatformsList().$MAP(it -> it.getName()).$TO_SET)")
    @Mapping(target = "genresList", expression = "java(game.getGenresList().$MAP(it -> it.getName()).$TO_SET)")
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
    // Set each set
    @Mapping(target = "privilegesList", expression = "java(privilegesList)")
    // Set each list as empty
    @Mapping(target = "usersList", expression = EMPTY_LIST)
    fun roleRequestToRole(
        roleRequest: RoleRequest,
        @Context privilegesList: Set<Privilege>
    ): Role
    @Mapping(target = "privilegesList", expression = "java(role.getPrivilegesList().$MAP(it -> it.getName()).$TO_SET)")
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
    @Mapping(target = "role", expression = "java(role)")
    // Set each list as empty
    @Mapping(target = "gameLogsList", expression = EMPTY_LIST)
    @Mapping(target = "logsList", expression = EMPTY_LIST)
    @Mapping(target = "reviewsList", expression = EMPTY_LIST)
    fun userRequestToUser(
        userRequest: UserRequest,
        @Context role: Role
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
// Game Log Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface GameLogMapper {
    // Mapping the variables not submitted
    @Mapping(target = "createdDate", expression = "java($UTILS_PATH.getCurrentDate())")
    @Mapping(target = "game", expression = "java(game)")
    @Mapping(target = "user", expression = "java(user)")
    @Mapping(target = "platform", expression = "java(platform)")
    fun gameLogRequestToGameLog(
        gameLogRequest: GameLogRequest,
        @Context game: Game,
        @Context user: User,
        @Context platform: Platform
    ): GameLog
    // Mapping the variables with clipped information
    @Mapping(target = "createdDate", expression = "java($UTILS_PATH.getDateAsString(gameLog.getCreatedDate()))")
    @Mapping(target = "finished", expression = "java($UTILS_PATH.getDateAsString(gameLog.getFinished()))")
    @Mapping(target = "platinum", expression = "java($UTILS_PATH.getDateAsString(gameLog.getPlatinum()))")
    @Mapping(target = "game.platformsList", expression = "java(game.getPlatformsList().$MAP(it -> it.getName()).$TO_SET)")
    @Mapping(target = "game.genresList", expression = "java(game.getGenresList().$MAP(it -> it.getName()).$TO_SET)")
    @Mapping(target = "user", expression = "java(gameLog.getUser().getName())")
    @Mapping(target = "platform", expression = "java(gameLog.getPlatform().getName())")
    @Mapping(target = "review", expression = "java(gameLog.getReview() != null ? gameLog.getReview().getDescription() : null)")
    fun gameLogToGameLogResponse(
        gameLog: GameLog
    ): GameLogResponse
    fun gameLogsListToGameLogResponsesList(
        gameLogsList: List<GameLog>
    ): List<GameLogResponse>
}
// Review Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ReviewMapper {
    // Mapping the variables not submitted
    @Mapping(target = "user", expression = "java(gameLog.getUser())")
    @Mapping(target = "game", expression = "java(gameLog.getGame())")
    @Mapping(target = "gameLog", expression = "java(gameLog)")
    fun contextToReview(
        description: String,
        @Context gameLog: GameLog
    ): Review
    // Mapping the variables with clipped information
    @Mapping(target = "user", expression = "java(review.getUser().getName())")
    @Mapping(target = "game", expression = "java(review.getGame().getName())")
    fun reviewToReviewResponse(
        review: Review
    ): ReviewResponse
    fun reviewsListToReviewResponsesList(
        reviewsList: List<Review>
    ): List<ReviewResponse>
}
// Review Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ActionTypeMapper {
    // Mapping the variables not submitted
    @Mapping(target = "id", expression = "java($UTILS_PATH.getIdByName(actionTypeRequest.getName()))")
    @Mapping(target = "logsList", expression = EMPTY_LIST)
    fun actionTypeRequestToActionType(
        actionTypeRequest: ActionTypeRequest
    ): ActionType
    fun actionTypeToActionTypeResponse(
        actionType: ActionType
    ): ActionTypeResponse
}