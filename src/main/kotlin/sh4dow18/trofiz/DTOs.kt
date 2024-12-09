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
data class RoleRequest(
    var name: String,
    var privilegesList: List<String>
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
    var description: String,
    var enabled: Boolean
)
data class RoleResponse(
    var id: Long,
    var name: String,
    var privilegesList: Set<PrivilegeResponse>
)