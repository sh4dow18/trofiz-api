package sh4dow18.trofiz
/* Main Comments

* @Transactional is a Tag that establishes that is a Transactional Service Function. This one makes a transaction
* when this service function is in operation.

* */
// Services Requirements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
// Platform Service Interface where the functions to be used in
// Spring Abstract Platform Service are declared
interface PlatformService {
    fun findAll(userId: Long): List<PlatformResponse>
    fun insert(platformRequest: PlatformRequest): PlatformResponse
}
// Spring Abstract Platform Service
@Service
class AbstractPlatformService(
    // Platform Service Props
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val platformMapper: PlatformMapper,
    @Autowired
    val userRepository: UserRepository,
): PlatformService {
    override fun findAll(userId: Long): List<PlatformResponse> {
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, userId, "ver-plataformas")
        // Returns all Platforms as a Platform Responses List
        return platformMapper.platformsListToPlatformResponsesList(platformRepository.findAll())
    }
    @Transactional(rollbackFor = [ElementAlreadyExists::class])
    override fun insert(platformRequest: PlatformRequest): PlatformResponse {
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, platformRequest.userId, "agregar-plataformas")
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
    fun findAll(userId: Long): List<GenreResponse>
    fun insert(genreRequest: GenreRequest): GenreResponse
}
// Spring Abstract Genre Service
@Service
class AbstractGenreService(
    // Genre Service Props
    @Autowired
    val genreRepository: GenreRepository,
    @Autowired
    val genreMapper: GenreMapper,
    @Autowired
    val userRepository: UserRepository
): GenreService {
    override fun findAll(userId: Long): List<GenreResponse> {
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, userId, "ver-géneros")
        // Returns all Genres as a Genre Responses List
        return genreMapper.genresListToGenreResponsesList(genreRepository.findAll())
    }
    @Transactional(rollbackFor = [ElementAlreadyExists::class])
    override fun insert(genreRequest: GenreRequest): GenreResponse {
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, genreRequest.userId, "agregar-géneros")
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
    fun findTop10ByNameContainingIgnoreCase(name: String): List<GameResponse>
    fun findAllReviewsById(id: String): List<ReviewResponse>
    fun findById(id: String): GameResponse
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
    val genreRepository: GenreRepository,
    @Autowired
    val reviewMapper: ReviewMapper
): GameService {
    override fun findAll(): List<GameResponse> {
        // Transforms a Games List to a Game Responses List
        return gameMapper.gamesListToGameResponsesList(gameRepository.findAll())
    }
    override fun findTop10ByNameContainingIgnoreCase(name: String): List<GameResponse> {
        // Transforms the first 10 Games from a Games List to a Game Responses List
        return gameMapper.gamesListToGameResponsesList(gameRepository.findTop10ByNameContainingIgnoreCase(name))
    }
    override fun findAllReviewsById(id: String): List<ReviewResponse> {
        // Check if the game already exists
        val game = gameRepository.findById(id).orElseThrow {
            NoSuchElementExists(id, "Juego")
        }
        // Transforms a Reviews List to a Review Responses List
        return reviewMapper.reviewsListToReviewResponsesList(game.reviewsList)
    }
    override fun findById(id: String): GameResponse {
        // Find a game with the id and if the game is not found, throw a "No Such Element Exists" error
        val game = gameRepository.findById(id).orElseThrow {
            NoSuchElementExists(id, "Juego")
        }
        // Transforms a Game to a Game Response
        return gameMapper.gameToGameResponse(game)
    }
    @Transactional(rollbackFor = [ElementAlreadyExists::class, NoSuchElementsExists::class])
    override fun insert(gameRequest: GameRequest): GameResponse {
        // Verifies if the game already exists
        if (gameRepository.findById(getIdByName(gameRequest.name)).orElse(null) != null) {
            throw ElementAlreadyExists(gameRequest.name, "Juego")
        }
        // Check if each platform submitted exists
        val platformsList = platformRepository.findAllById(gameRequest.platformsList)
        if (platformsList.size != gameRequest.platformsList.size) {
            val missingIds = gameRequest.platformsList - platformsList.map { it.id }.toSet()
            throw NoSuchElementsExists(missingIds.toList(), "Plataformas")
        }
        // Check if each genre submitted exists
        val genresList = genreRepository.findAllById(gameRequest.genresList)
        if (genresList.size != gameRequest.genresList.size) {
            val missingIds = gameRequest.genresList - genresList.map { it.id }.toSet()
            throw NoSuchElementsExists(missingIds.toList(), "Géneros")
        }
        // If the game not exists and each platform and genre exists, create the new game
        val newGame = gameMapper.gameRequestToGame(gameRequest, platformsList.toSet(), genresList.toSet())
        // Transforms the New Game to a Game Response and Returns it
        return gameMapper.gameToGameResponse(gameRepository.save(newGame))
    }
}
// Privilege Service Interface where the functions to be used in
// Spring Abstract Privilege Service are declared
interface PrivilegeService {
    fun findAll(userId: Long): List<PrivilegeResponse>
    fun insert(privilegeRequest: PrivilegeRequest): PrivilegeResponse
    fun update(updatePrivilegeRequest: UpdatePrivilegeRequest): PrivilegeResponse
}
// Spring Abstract Game Service
@Service
class AbstractPrivilegeService(
    // Privilege Service Props
    @Autowired
    val privilegeRepository: PrivilegeRepository,
    @Autowired
    val privilegeMapper: PrivilegeMapper,
    @Autowired
    val userRepository: UserRepository,
): PrivilegeService {
    override fun findAll(userId: Long): List<PrivilegeResponse> {
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, userId, "ver-privilegios")
        // Transforms a Privilege List to a Privilege Responses List
        return privilegeMapper.privilegesListToPrivilegeResponsesList(privilegeRepository.findAll())
    }
    @Transactional(rollbackFor = [ElementAlreadyExists::class])
    override fun insert(privilegeRequest: PrivilegeRequest): PrivilegeResponse {
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, privilegeRequest.userId, "agregar-privilegios")
        // Transforms Name in Privilege Request in lowercase and replace spaces with "-"
        // Example: "Add Games" -> "add-games"
        val privilegeId = getIdByName(privilegeRequest.name)
        // Verifies if the Privilege already exists
        if (privilegeRepository.findById(privilegeId).orElse(null) != null) {
            throw ElementAlreadyExists(privilegeId, "Privilegio")
        }
        // If not exists, create the new Privilege
        val newPrivilege = privilegeMapper.privilegeRequestToPrivilege(privilegeRequest)
        // Transforms the New Privilege to Privilege Response
        return privilegeMapper.privilegeToPrivilegeResponse(privilegeRepository.save(newPrivilege))
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class])
    override fun update(updatePrivilegeRequest: UpdatePrivilegeRequest): PrivilegeResponse {
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, updatePrivilegeRequest.userId, "actualizar-privilegios")
        // Verifies if the Privilege already exists
        val privilege = privilegeRepository.findById(updatePrivilegeRequest.id).orElseThrow {
            NoSuchElementExists(updatePrivilegeRequest.id,"Privilegio")
        }
        // Change enabled from true to false and vice versa
        privilege.enabled = !privilege.enabled
        // Transforms the Privilege to Privilege Response
        return privilegeMapper.privilegeToPrivilegeResponse(privilegeRepository.save(privilege))
    }
}
// Role Service Interface where the functions to be used in
// Spring Abstract Role Service are declared
interface RoleService {
    fun findAll(): List<RoleResponse>
    fun insert(roleRequest: RoleRequest): RoleResponse
    fun update(updateRoleRequest: UpdateRoleRequest): RoleResponse
}
// Spring Abstract Game Service
@Service
class AbstractRoleService(
    // Role Service Props
    @Autowired
    val roleRepository: RoleRepository,
    @Autowired
    val privilegeRepository: PrivilegeRepository,
    @Autowired
    val roleMapper: RoleMapper
): RoleService {
    override fun findAll(): List<RoleResponse> {
        // Transforms a Role List to a Role Responses List
        return roleMapper.rolesListToRoleResponsesList(roleRepository.findAll())
    }
    @Transactional(rollbackFor = [ElementAlreadyExists::class, NoSuchElementsExists::class])
    override fun insert(roleRequest: RoleRequest): RoleResponse {
        // Verifies if the Role already exists
        if (roleRepository.findByNameIgnoringCase(roleRequest.name).orElse(null) != null) {
            throw ElementAlreadyExists(roleRequest.name, "Rol")
        }
        // Check if the privileges on the submitted role already exist
        val privilegesList = privilegeRepository.findAllById(roleRequest.privilegesList)
        if (privilegesList.size != roleRequest.privilegesList.size) {
            val missingIds = roleRequest.privilegesList - privilegesList.map { it.id }.toSet()
            throw NoSuchElementsExists(missingIds.toList(), "Privilegios")
        }
        // If each privileges exist, create the new role
        val newRole = roleMapper.roleRequestToRole(roleRequest, privilegesList.toMutableSet())
        // Transforms the New Role to Role Response
        return roleMapper.roleToRoleResponse(roleRepository.save(newRole))
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class, NoSuchElementsExists::class])
    override fun update(updateRoleRequest: UpdateRoleRequest): RoleResponse {
        // Verifies if the Role already exists
        val role = roleRepository.findById(updateRoleRequest.id).orElseThrow {
            NoSuchElementExists("${updateRoleRequest.id}", "Rol")
        }
        // Check if the privileges on the submitted role already exist
        val privilegesList = privilegeRepository.findAllById(updateRoleRequest.privilegesList)
        if (privilegesList.size != updateRoleRequest.privilegesList.size) {
            val missingIds = updateRoleRequest.privilegesList - privilegesList.map { it.id }.toSet()
            throw NoSuchElementsExists(missingIds.toList(), "Privilegios")
        }
        // If the Role exists and the Privileges Exists, update it
        role.privilegesList = privilegesList.toMutableSet()
        // Transforms the New Role to Role Response
        return roleMapper.roleToRoleResponse(roleRepository.save(role))
    }
}
// User Service Interface where the functions to be used in
// Spring Abstract User Service are declared
interface UserService {
    fun findAll(): List<UserResponse>
    fun findAllReviewsById(id: Long): List<ReviewResponse>
    fun findAllLogsById(id: Long): List<LogResponse>
    fun findById(id: Long): UserResponse
    fun insert(userRequest: UserRequest): UserResponse
    fun update(updateUserRequest: UpdateUserRequest, image: MultipartFile?): UserResponse
    fun changeRole(changeRoleUserRequest: ChangeRoleUserRequest): UserResponse
    fun closeAccount(id: Long): UserResponse
}
// Spring Abstract Game Service
@Service
class AbstractUserService(
    // User Tests Props
    @Value("\${files.path}")
    val filesPath: String,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val userMapper: UserMapper,
    @Autowired
    val roleRepository: RoleRepository,
    @Autowired
    val reviewMapper: ReviewMapper,
    @Autowired
    val logMapper: LogMapper
): UserService {
    override fun findAll(): List<UserResponse> {
        // Transforms a User List to a User Responses List
        return userMapper.usersListToUserResponsesList(userRepository.findAll())
    }
    override fun findAllReviewsById(id: Long): List<ReviewResponse> {
        // Check if the game already exists
        val user = userRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Usuario")
        }
        // Transforms a Reviews List to a Review Responses List
        return reviewMapper.reviewsListToReviewResponsesList(user.reviewsList)
    }
    override fun findAllLogsById(id: Long): List<LogResponse> {
        // Check if the user already exists
        val user = userRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Usuario")
        }
        // Transforms a Logs List to a Log Responses List
        return logMapper.logsListToLogsResponsesList(user.logsList)
    }
    override fun findById(id: Long): UserResponse {
        // Verifies if the User already exists
        val user = userRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Usuario")
        }
        // If exists, transforms it to User Response
        return userMapper.userToUserResponse(user)
    }
    @Transactional(rollbackFor = [ElementAlreadyExists::class, NoSuchElementExists::class])
    override fun insert(userRequest: UserRequest): UserResponse {
        // Verifies if the User already exists
        val user = userRepository.findByEmailOrName(userRequest.email, userRequest.name).orElse(null)
        if (user != null) {
            val prop = if (user.email == userRequest.email) user.email else user.name
            throw ElementAlreadyExists(prop!!, "Usuario")
        }
        // Check if the username length is less than 16 characters
        if (userRequest.name.length > 16) {
            throw BadRequest("El nombre de usuario debe ser menor a 16 caracteres")
        }
        // Check if the "Gamer" role already exist
        val role = roleRepository.findByNameIgnoringCase("Gamer").orElseThrow {
            NoSuchElementExists("Gamer", "Rol")
        }
        // If the role exist, create the new User
        val newUser = userMapper.userRequestToUser(userRequest, role)
        // Transforms the New User to User Response
        return userMapper.userToUserResponse(userRepository.save(newUser))
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class, ElementAlreadyExists::class, BadRequest::class])
    override fun update(updateUserRequest: UpdateUserRequest, image: MultipartFile?): UserResponse {
        // Verifies if the User already exists
        val user = userRepository.findById(updateUserRequest.id).orElseThrow {
            NoSuchElementExists("${updateUserRequest.id}", "Usuario")
        }
        if (updateUserRequest.name != null) {
            // Check if the username length is less than 16 characters
            if (updateUserRequest.name!!.length > 16) {
                throw BadRequest("El nombre de usuario debe ser menor a 16 caracteres")
            }
            // Check if another user has the same name
            val anotherUser = userRepository.findByEmailOrName("", updateUserRequest.name!!).orElse(null)
            if (anotherUser != null) {
                throw ElementAlreadyExists(updateUserRequest.name!!, "Usuario")
            }
            // Update the user
            user.name = updateUserRequest.name!!
        }
        // Check if the image was submitted
        if (image != null) {
            // Verifies if the image file is really an Image
            if (ImageIO.read(image.inputStream) == null) {
                throw BadRequest("Image file type not supported")
            }
            // Create a New Image 150 x 150 from the original image
            val originalImage = ImageIO.read(image.inputStream)
            val targetWidth = 150
            val targetHeight = 150
            val xScale = targetWidth.toDouble() / originalImage.width
            val yScale = targetHeight.toDouble() / originalImage.height
            val scale = if (xScale > yScale) xScale else yScale
            val newWidth = (originalImage.width * scale).toInt()
            val newHeight = (originalImage.height * scale).toInt()
            val resizedImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB)
            val graphics = resizedImage.createGraphics()
            graphics.clip = Ellipse2D.Float(0f, 0f, targetWidth.toFloat(), targetHeight.toFloat())
            graphics.drawImage(originalImage, (targetWidth - newWidth) / 2, (targetHeight - newHeight) / 2, newWidth, newHeight, null)
            graphics.dispose()
            // Add the "User" email plus ".png"
            val fileName = user.email + ".png"
            // Renders the image as "jpg" and save it in the path "filesPath"
            ImageIO.write(resizedImage, "png", File("$filesPath/users/$fileName"))
            user.image = true
        }
        // Transforms the User to User Response
        return userMapper.userToUserResponse(userRepository.save(user))
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class])
    override fun changeRole(changeRoleUserRequest: ChangeRoleUserRequest): UserResponse {
        // Verifies if the User already exists
        val user = userRepository.findById(changeRoleUserRequest.userId).orElseThrow {
            NoSuchElementExists("${changeRoleUserRequest.userId}", "Usuario")
        }
        // Verifies if the Role already exists
        val role = roleRepository.findById(changeRoleUserRequest.roleId).orElseThrow {
            NoSuchElementExists("${changeRoleUserRequest.roleId}", "Rol")
        }
        // If the user and role were found, update the user
        user.role = role
        // Transforms the User to User Response
        return userMapper.userToUserResponse(userRepository.save(user))
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class])
    override fun closeAccount(id: Long): UserResponse {
        // Verifies if the User already exists
        val user = userRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Usuario")
        }
        // Update user
        // Check if the user has a profile image, if exists, delete it
        if (user.image) {
            val image = File("$filesPath/users/${user.email}.png")
            val imageDelete = image.delete()
            if (imageDelete) {
                user.image = false
            }
        }
        // Delete all the user's personal information
        user.email = null
        user.name = null
        user.password = null
        user.enabled = false
        // Transforms the User to User Response
        return userMapper.userToUserResponse(userRepository.save(user))
    }
}
// Game Log Service Interface where the functions to be used in
// Spring Abstract Game Log Service are declared
interface GameLogService {
    fun findAll(): List<GameLogResponse>
    fun findByUserId(id: Long): List<GameLogResponse>
    fun findById(id: Long): GameLogResponse
    fun insert(gameLogRequest: GameLogRequest): GameLogResponse
    fun update(updateGameLogRequest: UpdateGameLogRequest): GameLogResponse
    fun delete(deleteGameLogRequest: DeleteGameLogRequest): String
}
// Spring Abstract Game Log Service
@Service
class AbstractGameLogService(
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
    val reviewRepository: ReviewRepository,
    @Autowired
    val reviewMapper: ReviewMapper
): GameLogService {
    override fun findAll(): List<GameLogResponse> {
        // Transforms the Game Logs List to a Game Log Responses List
        return gameLogMapper.gameLogsListToGameLogResponsesList(gameLogRepository.findAll())
    }
    override fun findByUserId(id: Long): List<GameLogResponse> {
        // Transforms the Game Logs List to a Game Log Responses List
        return gameLogMapper.gameLogsListToGameLogResponsesList(gameLogRepository.findByUserIdOrderByCreatedDateAsc(id))
    }
    override fun findById(id: Long): GameLogResponse {
        // Check if the game log already exists
        val gameLog = gameLogRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Registro de Juego")
        }
        // Transforms the Game Logs List to a Game Log Responses List
        return gameLogMapper.gameLogToGameLogResponse(gameLog)
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class, ElementAlreadyExists::class])
    override fun insert(gameLogRequest: GameLogRequest): GameLogResponse {
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
        return gameLogMapper.gameLogToGameLogResponse(gameLogRepository.save(newGameLog))
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class])
    override fun update(updateGameLogRequest: UpdateGameLogRequest): GameLogResponse {
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
                gameLog.review = reviewRepository.save(newReview)
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
        return gameLogMapper.gameLogToGameLogResponse(gameLogRepository.save(gameLog))
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class])
    override fun delete(deleteGameLogRequest: DeleteGameLogRequest): String {
        // Check if the user submitted already exists
        val gameLog = gameLogRepository.findById(deleteGameLogRequest.id).orElseThrow {
            NoSuchElementExists("${deleteGameLogRequest.id}", "Registro de Juego")
        }
        // Delete the Game Log
        if (gameLog.review != null) {
            reviewRepository.delete(gameLog.review!!)
        }
        gameLogRepository.delete(gameLog)
        return "El Registro de Juego con el Identificador ${deleteGameLogRequest.id} fue eliminado con éxito"
    }
}
// Action Type Service Interface where the functions to be used in
// Spring Abstract Action Type Service are declared
interface ActionTypeService {
    fun findAll(): List<ActionTypeResponse>
    fun insert(actionTypeRequest: ActionTypeRequest): ActionTypeResponse
}
// Spring Abstract Action Type Service
@Service
class AbstractActionTypeService(
    // Action Type Tests Props
    @Autowired
    val actionTypeRepository: ActionTypeRepository,
    @Autowired
    val actionTypeMapper: ActionTypeMapper,
    @Autowired
    val userRepository: UserRepository,
): ActionTypeService {
    override fun findAll(): List<ActionTypeResponse> {
        // Transforms the Action Types List to a Action Types Responses List
        return actionTypeMapper.actionTypesListToActionTypeResponsesList(actionTypeRepository.findAll())
    }
    @Transactional(rollbackFor = [ElementAlreadyExists::class])
    override fun insert(actionTypeRequest: ActionTypeRequest): ActionTypeResponse {
        // Check if the submitted user could do the submitted action
        checkUserValidation(userRepository, actionTypeRequest.userId, "agregar-tipos-de-acción")
        // Check if the Action Type submitted already exists
        val id = getIdByName(actionTypeRequest.name)
        if (actionTypeRepository.findById(id).orElse(null) != null) {
            throw ElementAlreadyExists(id, "Tipo de Acción")
        }
        // If the Action Type exists, create a new Action Type
        val newActionType = actionTypeMapper.actionTypeRequestToActionType(actionTypeRequest)
        // Transforms the New Action Type to a Action Type Response
        return actionTypeMapper.actionTypeToActionTypeResponse(actionTypeRepository.save(newActionType))
    }
}
// Log Service Interface where the functions to be used in
// Spring Abstract Log Service are declared
interface LogService {
    fun findAll(): List<LogResponse>
    fun insert(logRequest: LogRequest): LogResponse
}
// Spring Abstract Log Service
@Service
class AbstractLogService(
    // Log Props
    @Autowired
    val logRepository: LogRepository,
    @Autowired
    val logMapper: LogMapper,
    @Autowired
    val actionTypeRepository: ActionTypeRepository,
    @Autowired
    val userRepository: UserRepository
): LogService {
    override fun findAll(): List<LogResponse> {
        // Transforms the Logs List to a Logs Responses List
        return logMapper.logsListToLogsResponsesList(logRepository.findAll())
    }
    @Transactional(rollbackFor = [NoSuchElementExists::class])
    override fun insert(logRequest: LogRequest): LogResponse {
        // Check if the Action Type submitted already exists
        val actionType = actionTypeRepository.findById(logRequest.actionTypeId).orElseThrow {
            NoSuchElementExists(logRequest.actionTypeId, "Tipo de Acción")
        }
        // Check if the user submitted already exists
        val user = userRepository.findById(logRequest.userId).orElseThrow {
            NoSuchElementExists("${logRequest.userId}", "Usuario")
        }
        // If the action type and user exists, create a new log
        val newLog = logMapper.logRequestToLog(logRequest, actionType, user)
        // Transforms the New Log to a Log Response
        return logMapper.logToLogResponse(logRepository.save(newLog))
    }
}