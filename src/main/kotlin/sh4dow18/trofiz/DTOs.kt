package sh4dow18.trofiz
// Interfaces
interface NamedEntity {
    val name: String
}
// Requests
data class PlatformRequest(
    override var name: String,
): NamedEntity
data class GenreRequest(
    override var name: String
): NamedEntity
data class GameRequest(
    var name: String,
    var rating: Float,
    var metacritic: Int,
    var releaseDate: String,
    var imageUrl: String,
    var platformsList: Set<PlatformRequest>,
    var genresList: Set<GenreRequest>
)
data class PrivilegeRequest(
    var name: String,
    var description: String
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
data class GameResponse(
    var id: String,
    var name: String,
    var rating: Float,
    var metacritic: Int,
    var releaseDate: String,
    var imageUrl: String,
    var platformsList: Set<PlatformResponse>,
    var genresList: Set<GenreResponse>
)
data class PrivilegeResponse(
    var id: String,
    var name: String,
    var description: String
)