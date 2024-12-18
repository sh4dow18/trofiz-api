package sh4dow18.trofiz
// Rest Controllers Requirements
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
// Platform Rest controller main class
@RestController
@RequestMapping("\${endpoint.platforms}")
class PlatformRestController(private val platformService: PlatformService) {
    // When the Endpoint has HTTP GET requests, call find all platforms function
    @GetMapping
    @ResponseBody
    fun findAll() = platformService.findAll()
    // When the Endpoint has HTTP POST requests, call insert platform function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody platformRequest: PlatformRequest) = platformService.insert(platformRequest)
}
// Genre Rest controller main class
@RestController
@RequestMapping("\${endpoint.genres}")
class GenreRestController(private val genreService: GenreService) {
    // When the Endpoint has HTTP GET requests, call find all genres function
    @GetMapping
    @ResponseBody
    fun findAll() = genreService.findAll()
    // When the Endpoint has HTTP POST requests, call insert genre function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody genreRequest: GenreRequest) = genreService.insert(genreRequest)
}
// Game Rest controller main class
@RestController
@RequestMapping("\${endpoint.games}")
class GameRestController(private val gameService: GameService) {
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
    fun findTop10ByNameContainingIgnoreCase(@PathVariable name: String) = gameService.findTop10ByNameContainingIgnoreCase(name)
    // When the Endpoint has HTTP POST requests, call insert game function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody gameRequest: GameRequest) = gameService.insert(gameRequest)
}
// Privilege Rest controller main class
@RestController
@RequestMapping("\${endpoint.privileges}")
class PrivilegeRestController(private val privilegeService: PrivilegeService) {
    // When the Endpoint has HTTP GET requests, call find all Privileges function
    @GetMapping
    @ResponseBody
    fun findAll() = privilegeService.findAll()
    // When the Endpoint has HTTP POST requests, call insert Privilege function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody privilegeRequest: PrivilegeRequest) = privilegeService.insert(privilegeRequest)
    // When the Endpoint has HTTP PUT requests, call update status Privilege function
    @PutMapping("status/{id}")
    @ResponseBody
    fun updateStatus(@PathVariable id: String) = privilegeService.updateStatus(id)
}
// Privilege Rest controller main class
@RestController
@RequestMapping("\${endpoint.roles}")
class RoleRestController(private val roleService: RoleService) {
    // When the Endpoint has HTTP GET requests, call find all Roles function
    @GetMapping
    @ResponseBody
    fun findAll() = roleService.findAll()
    // When the Endpoint has HTTP POST requests, call insert Role function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody roleRequest: RoleRequest) = roleService.insert(roleRequest)
    // When the Endpoint has HTTP POST requests, call update Role function
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestBody updateRoleRequest: UpdateRoleRequest) = roleService.update(updateRoleRequest)
}
// User Rest controller main class
@RestController
@RequestMapping("\${endpoint.users}")
class UserRestController(private val userService: UserService) {
    // When the Endpoint has HTTP GET requests, call find all Users function
    @GetMapping
    @ResponseBody
    fun findAll() = userService.findAll()
    // When the Endpoint has HTTP GET requests with an id, call find user by id function
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable("id") id: Long) = userService.findById(id)
    // When the Endpoint has HTTP POST requests, call insert User function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody userRequest: UserRequest) = userService.insert(userRequest)
    // When the Endpoint has HTTP PUT requests, call Update User function
    @PutMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestPart("information") updateUserRequest: UpdateUserRequest,
               @RequestPart("image") image: MultipartFile?) = userService.update(updateUserRequest, image)
    // When the Endpoint has HTTP PUT requests with subdirectory "close" and id, call Close User's Account function
    @PutMapping("close/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun closeAccount(@PathVariable("id") id: Long) = userService.closeAccount(id)
}
// Game Log Rest controller main class
@RestController
@RequestMapping("\${endpoint.gameLogs}")
class GameLogRestController(private val gameLogService: GameLogService) {
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
    fun insert(@RequestBody gameLogRequest: GameLogRequest) = gameLogService.insert(gameLogRequest)
    // When the Endpoint has HTTP POST requests, call insert Game Log function
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestBody updateGameLogRequest: UpdateGameLogRequest) = gameLogService.update(updateGameLogRequest)
    // When the Endpoint has HTTP POST requests, call delete Game Log function
    @DeleteMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun delete(@RequestBody deleteGameLogRequest: DeleteGameLogRequest) = gameLogService.delete(deleteGameLogRequest)
}