package sh4dow18.trofiz
// Rest Controllers Requirements
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

// Public Rest Controllers

// Platform Public Rest controller main class
@RestController
@RequestMapping("\${endpoint.public.platforms}")
class PlatformPublicRestController(private val platformService: PlatformService) {
    // When the Endpoint has HTTP GET requests, call find all platforms function
    @GetMapping
    @ResponseBody
    fun findAll() = platformService.findAll()
}
// Genre Public Rest controller main class
@RestController
@RequestMapping("\${endpoint.public.genres}")
class GenrePublicRestController(private val genreService: GenreService) {
    // When the Endpoint has HTTP GET requests, call find all genres function
    @GetMapping
    @ResponseBody
    fun findAll() = genreService.findAll()
}
// Game Public Rest controller main class
@RestController
@RequestMapping("\${endpoint.public.games}")
class GamePublicRestController(private val gameService: GameService) {
    // When the Endpoint has HTTP GET requests, call find all games function
    @GetMapping
    @ResponseBody
    fun findAll() = gameService.findAll()
    // When the Endpoint has HTTP GET requests with an id, call find by id function
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id: String) = gameService.findById(id)
    // When the Endpoint has HTTP GET requests on "search" and an id, call find Top 10 by name containing ignore case function
    @GetMapping("search/{name}")
    @ResponseBody
    fun findTop10ByNameContainingIgnoreCase(@PathVariable name: String) =
        gameService.findTop10ByNameContainingIgnoreCase(name)
    // When the Endpoint has HTTP GET requests on "reviews" and an id, call find all reviews by id function
    @GetMapping("{id}/reviews")
    @ResponseBody
    fun findAllReviewsById(@PathVariable id: String) =
        gameService.findAllReviewsById(id)
}
// User Public Rest controller main class
@RestController
@RequestMapping("\${endpoint.public.users}")
class UserPublicRestController(
    private val userService: UserService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(UserPublicRestController::class.java)
    // When the Endpoint has HTTP POST requests, call insert User function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody userRequest: UserRequest): UserResponse {
        val response = userService.insert(userRequest)
        addLog(logService, "Usuario '${response.name}' con el Rol ${response.role}", "inserción", logger)
        return response
    }
}

// Private Rest Controllers

// Platform Rest controller main class
@RestController
@RequestMapping("\${endpoint.platforms}")
class PlatformRestController(
    private val platformService: PlatformService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(PlatformRestController::class.java)
    // When the Endpoint has HTTP POST requests, call insert platform function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody platformRequest: PlatformRequest): PlatformResponse {
        val response = platformService.insert(platformRequest)
        addLog(logService, "Plataforma '${platformRequest.name}'", "inserción", logger)
        return response
    }
}
// Genre Rest controller main class
@RestController
@RequestMapping("\${endpoint.genres}")
class GenreRestController(
    private val genreService: GenreService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(GenreRestController::class.java)
    // When the Endpoint has HTTP POST requests, call insert genre function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody genreRequest: GenreRequest): GenreResponse {
        val response = genreService.insert(genreRequest)
        addLog(logService, "Género '${genreRequest.name}'", "inserción", logger)
        return response
    }
}
// Game Rest controller main class
@RestController
@RequestMapping("\${endpoint.games}")
class GameRestController(
    private val gameService: GameService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(GameRestController::class.java)
    // When the Endpoint has HTTP POST requests, call insert game function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody gameRequest: GameRequest): GameResponse {
        val response = gameService.insert(gameRequest)
        addLog(logService, "Juego '${gameRequest.name}'", "inserción", logger)
        return response
    }
}
// Privilege Rest controller main class
@RestController
@RequestMapping("\${endpoint.privileges}")
class PrivilegeRestController(
    private val privilegeService: PrivilegeService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(PrivilegeRestController::class.java)
    // When the Endpoint has HTTP GET requests, call find all Privileges function
    @GetMapping
    @ResponseBody
    fun findAll() = privilegeService.findAll()
    // When the Endpoint has HTTP POST requests, call insert Privilege function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody privilegeRequest: PrivilegeRequest): PrivilegeResponse {
        val response = privilegeService.insert(privilegeRequest)
        addLog(logService, "Privilegio '${response.name}'", "inserción", logger)
        return response
    }
    // When the Endpoint has HTTP PUT requests, call update Privilege function
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestBody updatePrivilegeRequest: UpdatePrivilegeRequest): PrivilegeResponse {
        val response = privilegeService.update(updatePrivilegeRequest)
        addLog(logService, "Estado del Privilegio '${response.name}' a ${response.enabled}",
            "actualización", logger)
        return response
    }
}
// Privilege Rest controller main class
@RestController
@RequestMapping("\${endpoint.roles}")
class RoleRestController(
    private val roleService: RoleService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(RoleRestController::class.java)
    // When the Endpoint has HTTP GET requests, call find all Roles function
    @GetMapping
    @ResponseBody
    fun findAll() = roleService.findAll()
    // When the Endpoint has HTTP POST requests, call insert Role function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody roleRequest: RoleRequest): RoleResponse {
        val response = roleService.insert(roleRequest)
        addLog(logService, "Role '${response.name}' con los Privilegios ${response.privilegesList}",
            "inserción", logger)
        return response
    }
    // When the Endpoint has HTTP POST requests, call update Role function
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestBody updateRoleRequest: UpdateRoleRequest): RoleResponse {
        val response = roleService.update(updateRoleRequest)
        addLog(logService, "Privilegios del Rol '${response.name}' por ${response.privilegesList}",
            "actualización", logger)
        return response
    }
}
// User Rest controller main class
@RestController
@RequestMapping("\${endpoint.users}")
class UserRestController(
    private val userService: UserService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(UserRestController::class.java)
    // When the Endpoint has HTTP GET requests, call find all Users function
    @GetMapping
    @ResponseBody
    fun findAll() = userService.findAll()
    // When the Endpoint has HTTP GET requests on "reviews" and an id, call find all reviews by id function
    @GetMapping("{id}/reviews")
    @ResponseBody
    fun findAllReviewsById(@PathVariable id: Long) = userService.findAllReviewsById(id)
    // When the Endpoint has HTTP GET requests on "reviews" and an id, call find all reviews by id function
    @GetMapping("{id}/logs")
    @ResponseBody
    fun findAllLogsById(@PathVariable id: Long) = userService.findAllLogsById(id)
    // When the Endpoint has HTTP GET requests with an id, call find user by id function
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable("id") id: Long) = userService.findById(id)
    // When the Endpoint has HTTP PUT requests, call Update User function
    @PutMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestPart("information") updateUserRequest: UpdateUserRequest,
               @RequestPart("image") image: MultipartFile?): UserResponse {
        val response = userService.update(updateUserRequest, image)
        val imageSent = if (image != null) " e Imagen" else ""
        addLog(logService,
            "Usuario '${response.name}' con Nueva Información ${updateUserRequest.toNonNullString()}${imageSent}",
            "actualización", logger)
        return response
    }
    // When the Endpoint has HTTP PUT requests with subdirectory "role", call Change Role function
    @PutMapping("role", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun changeRole(@RequestBody changeRoleUserRequest: ChangeRoleUserRequest): UserResponse {
        val response = userService.changeRole(changeRoleUserRequest)
        addLog(logService, "Rol de Usuario con Id '${changeRoleUserRequest.id}' a '${response.role}'",
            "actualización", logger)
        return response
    }
    // When the Endpoint has HTTP PUT requests with subdirectory "close" and id, call Close User's Account function
    @PutMapping("close/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun closeAccount(@PathVariable("id") id: Long): UserResponse {
        val response = userService.closeAccount(id)
        addLog(logService, "Cerrar cuenta de Usuario con Id '${id}'", "actualización", logger)
        return response
    }
}
// Game Log Rest controller main class
@RestController
@RequestMapping("\${endpoint.gameLogs}")
class GameLogRestController(
    private val gameLogService: GameLogService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(GameLogRestController::class.java)
    // When the Endpoint has HTTP GET requests, call find all Game Logs function
    @GetMapping
    @ResponseBody
    fun findAll() = gameLogService.findAll()
    // When the Endpoint has HTTP GET requests with subdirectory "user" and id, call find all Game Logs function
    @GetMapping("user/{id}")
    @ResponseBody
    fun findByUserId(@PathVariable("id") id: Long) = gameLogService.findByUserId(id)
    // When the Endpoint has HTTP GET requests with an id, call find Game Log by id function
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable("id") id: Long) = gameLogService.findById(id)
    // When the Endpoint has HTTP POST requests, call insert Game Log function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody gameLogRequest: GameLogRequest): GameLogResponse {
        val response = gameLogService.insert(gameLogRequest)
        addLog(logService, "Registro del Juego '${response.game.name}'", "inserción", logger)
        return response
    }
    // When the Endpoint has HTTP POST requests, call insert Game Log function
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestBody updateGameLogRequest: UpdateGameLogRequest): GameLogResponse {
        val response = gameLogService.update(updateGameLogRequest)
        addLog(logService,
            "Registro de Juego '${response.game.name}' con Nueva Información ${updateGameLogRequest.toNonNullString()}",
            "actualización", logger)
        return response
    }
    // When the Endpoint has HTTP POST requests, call delete Game Log function
    @DeleteMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun delete(@RequestBody deleteGameLogRequest: DeleteGameLogRequest): String {
        val response = gameLogService.delete(deleteGameLogRequest)
        addLog(logService,"Registro de Juego '${deleteGameLogRequest.id}'", "eliminación", logger)
        return response
    }
}
// Action Type Rest controller main class
@RestController
@RequestMapping("\${endpoint.actionType}")
class ActionTypeRestController(
    private val actionTypeService: ActionTypeService,
    private val logService: LogService
) {
    private val logger: Logger = LoggerFactory.getLogger(ActionTypeRestController::class.java)
    // When the Endpoint has HTTP GET requests, call find all Action Types function
    @GetMapping
    @ResponseBody
    fun findAll() = actionTypeService.findAll()
    // When the Endpoint has HTTP POST requests, call insert Action Type function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody actionTypeRequest: ActionTypeRequest): ActionTypeResponse {
        val response = actionTypeService.insert(actionTypeRequest)
        addLog(logService, "Tipo de Acción '${response.name}'", "inserción", logger)
        return response
    }
}
// Log Rest controller main class
@RestController
@RequestMapping("\${endpoint.logs}")
class LogRestController(private val logService: LogService) {
    // When the Endpoint has HTTP GET requests, call find all Logs function
    @GetMapping
    @ResponseBody
    fun findAll() = logService.findAll()
}