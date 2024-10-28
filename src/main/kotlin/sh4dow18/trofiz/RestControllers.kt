package sh4dow18.trofiz
// Rest Controllers Requirements
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
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