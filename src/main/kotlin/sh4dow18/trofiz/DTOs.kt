package sh4dow18.trofiz

import com.fasterxml.jackson.annotation.JsonCreator

// Requests
data class PlatformRequest(
    var name: String
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
data class UpdatePrivilegeRequest(
    var id: String
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
data class ChangeRoleUserRequest(
    var id: Long,
    var roleId: Long
)
data class GameLogRequest(
    var gameId: String,
    var userId: Long,
    var platformId: String,
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
    var id: Long
)
data class ActionTypeRequest(
    var name: String
)
data class LogRequest(
    var action: String,
    var actionTypeId: String
)
data class LoginRequest(
    var email: String,
    var password: String
){
    @JsonCreator
    constructor() : this("", "")
}
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
    var user: Long,
    var platform: String,
)
data class ReviewResponse(
    var id: Long,
    var description: String,
    var user: Long,
    var game: String,
)
data class ActionTypeResponse(
    var id: String,
    var name: String
)
data class LogResponse(
    var id: Long,
    var action: String,
    var actionType: String,
    var user: Long
)