package sh4dow18.trofiz

import java.time.ZonedDateTime

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
    var platformsList: Set<String>,
    var genresList: Set<String>
)
data class PrivilegeRequest(
    var name: String,
    var description: String
)
data class RoleRequest(
    var name: String,
    var privilegesList: List<String>
)
data class UpdateRoleRequest(
    var id: Long,
    var privilegesList: List<String>
)
data class UserRequest(
    var email: String,
    var userName: String,
    var password: String,
)
data class UpdateUserRequest(
    var id: Long,
    var userName: String?
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
    var platformsList: Set<String>,
    var genresList: Set<String>
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
    var privilegesList: Set<String>
)
data class UserResponse(
    var id: Long,
    var email: String?,
    var userName: String?,
    var createdDate: String,
    var enabled: Boolean,
    var image: Boolean,
    var role: String
)