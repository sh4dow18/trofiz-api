package sh4dow18.trofiz

import java.time.ZonedDateTime

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
    var name: String,
    var password: String,
)
data class UpdateUserRequest(
    var id: Long,
    var name: String?
)
data class GameLogRequest(
    var gameId: String,
    var userId: Long,
    var platformId: String
)
data class UpdateGameLogRequest(
    var id: Long,
    var rating: Float?,
    var createdDate: String?,
    var finished: String?,
    var platinum: String?,
    var review: String?,
    var platformId: String?
)
data class DeleteGameLogRequest(
    var id: Long,
    var userId: Long
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
    var name: String?,
    var createdDate: String,
    var enabled: Boolean,
    var image: Boolean,
    var role: String
)
data class GameLogResponse(
    var id: Long,
    var rating: Float,
    var createdDate: String,
    var finished: String?,
    var platinum: String?,
    var review: String?,
    var game: GameResponse,
    var user: String,
    var platform: String
)