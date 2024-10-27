package sh4dow18.trofiz
// Requests
data class PlatformRequest(
    var name: String,
)
// Responses
data class PlatformResponse(
    var id: Long,
    var name: String
)