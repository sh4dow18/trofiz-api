package sh4dow18.trofiz
// Requests
data class PlatformRequest(
    var name: String,
)
data class GenreRequest(
    var name: String
)
data class GameRequest(
    var name: String,
    var rating: Float,
    var metacritic: Int,
    var releaseDate: String,
    var imageUrl: String,
    var platformsList: Set<PlatformRequest>,
    var genresList: Set<GenreRequest>
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
data class GameLogResponse(
    var id: Long
)
data class GameResponse(
    var name: String,
    var rating: Float,
    var metacritic: Int,
    var releaseDate: String,
    var imageUrl: String,
    var gamesLogsList: List<GameLogResponse>,
    var platformsList: Set<PlatformResponse>,
    var genresList: Set<GenreResponse>
)