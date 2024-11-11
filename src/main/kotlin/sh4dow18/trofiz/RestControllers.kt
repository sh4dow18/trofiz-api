package sh4dow18.trofiz
// Rest Controllers Requirements
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
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