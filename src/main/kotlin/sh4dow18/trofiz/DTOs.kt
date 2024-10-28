package sh4dow18.trofiz
// Requests
data class PlatformRequest(
    var name: String,
)
data class GenreRequest(
    var name: String
)
// Responses
data class PlatformResponse(
    var id: String,
    var name: String
)
data class GenreResponse(
    var id: String,
    var name: String
)