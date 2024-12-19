package sh4dow18.trofiz
// Entities Requirements
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.ZonedDateTime
// User Entity
@Entity
@Table(name = "users")
data class User(
    // User Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var email: String?,
    var name: String?,
    var password: String?,
    var createdDate: ZonedDateTime,
    var enabled: Boolean,
    var image: Boolean,
    // User Relationships
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    var role: Role,
    @OneToMany(mappedBy = "user", targetEntity = GameLog::class)
    var gameLogsList: List<GameLog>,
    @OneToMany(mappedBy = "user", targetEntity = Review::class)
    var reviewsList: List<Review>,
    @OneToMany(mappedBy = "user", targetEntity = Log::class)
    var logsList: List<Log>
)
// Role Entity
@Entity
@Table(name = "roles")
data class Role(
    // Role Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    // Role Relationships
    @OneToMany(mappedBy = "role", targetEntity = User::class)
    var usersList: List<User>,
    @ManyToMany(targetEntity = Privilege::class)
    @JoinTable(
        name = "role_privilege",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privilegesList: Set<Privilege>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Role
        if (other !is Role) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()
}
// Privileges Entity
@Entity
@Table(name = "privileges")
data class Privilege(
    // Privileges Properties
    @Id
    var id: String,
    var name: String,
    var description: String,
    var enabled: Boolean,
    // Privileges Relationships
    @ManyToMany(mappedBy = "privilegesList", fetch = FetchType.LAZY, targetEntity = Role::class)
    var rolesList: Set<Role>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Privilege
        if (other !is Privilege) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()

}
// Game Log Entity
@Entity
@Table(name = "game_log")
data class GameLog(
    // Game Log Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var rating: Float,
    var createdDate: ZonedDateTime,
    var finished: ZonedDateTime?,
    var platinum: ZonedDateTime?,
    // Game Log Relationships
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false, referencedColumnName = "id")
    var game: Game,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    var user: User,
    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false, referencedColumnName = "id")
    var platform: Platform,
    @OneToOne(mappedBy = "gameLog", targetEntity = Review::class)
    var review: Review?,
)
// Game Entity
@Entity
@Table(name = "game")
data class Game(
    // Game Properties
    @Id
    var id: String,
    var name: String,
    var rating: Float,
    var metacritic: Int,
    var releaseDate: String,
    var imageUrl: String,
    // Game Relationships
    @OneToMany(mappedBy = "game", targetEntity = GameLog::class)
    var gameLogsList: List<GameLog>,
    @ManyToMany(targetEntity = Platform::class)
    @JoinTable(
        name = "game_platform",
        joinColumns = [JoinColumn(name = "game_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "platform_id", referencedColumnName = "id")]
    )
    var platformsList: Set<Platform>,
    @ManyToMany(targetEntity = Genre::class)
    @JoinTable(
        name = "game_genre",
        joinColumns = [JoinColumn(name = "game_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    var genresList: Set<Genre>,
    @OneToMany(mappedBy = "game", targetEntity = Review::class)
    var reviewsList: List<Review>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Game
        if (other !is Game) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()
}
// Platform Entity
@Entity
@Table(name = "platforms")
data class Platform(
    // Platform Properties
    @Id
    var id: String,
    var name: String,
    // Platform Relationships
    @OneToMany(mappedBy = "platform", targetEntity = GameLog::class)
    var gameLogsList: List<GameLog>,
    @ManyToMany(mappedBy = "platformsList", fetch = FetchType.LAZY, targetEntity = Game::class)
    var gamesList: Set<Game>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Platform
        if (other !is Platform) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()
}
// Game Genre Entity
@Entity
@Table(name = "genres")
data class Genre(
    // Genre Properties
    @Id
    var id: String,
    var name: String,
    // Genre Relationships
    @ManyToMany(mappedBy = "genresList", fetch = FetchType.LAZY, targetEntity = Game::class)
    var gamesList: Set<Game>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Genre
        if (other !is Genre) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()
}
// Review Entity
@Entity
@Table(name = "reviews")
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var description: String,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    var user: User,
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false, referencedColumnName = "id")
    var game: Game,
    @OneToOne
    @JoinColumn(name = "game_log_id", nullable = false, referencedColumnName = "id")
    var gameLog: GameLog
)
// Log Entity
@Entity
@Table(name = "logs")
data class Log(
    // Log Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var action: String,
    // Log Relationships
    @ManyToOne
    @JoinColumn(name = "action_type_id", nullable = false, referencedColumnName = "id")
    var actionType: ActionType,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    var user: User
)
// Log Action Type Entity
@Entity
@Table(name = "action_types")
data class ActionType(
    // Action Type Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    // Action Type Relationships
    @OneToMany(mappedBy = "actionType", targetEntity = Log::class)
    var logsList: List<Log>
)