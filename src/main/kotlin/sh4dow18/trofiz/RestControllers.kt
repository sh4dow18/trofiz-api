package sh4dow18.trofiz
// Rest Controllers Requirements
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
// Platform Rest controller main class
@RestController
@RequestMapping("\${endpoint.platforms}")
class PlatformRestController(private val platformService: PlatformService) {
    // When the Endpoint has HTTP POST requests, call insert platform function
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun insert(@RequestBody platformRequest: PlatformRequest) = platformService.insert(platformRequest)
}