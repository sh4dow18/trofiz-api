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
import jakarta.persistence.Table
import java.time.ZonedDateTime
// User Entity
@Entity
@Table(name = "users")
data class User(
    // User Properties
    @Id
    var email: String,
    var userName: String,
    var password: String?,
    var createdDate: ZonedDateTime,
    var enabled: Boolean,
    var image: Boolean,
    // User Relationships
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    var role: Role,
    @OneToMany(mappedBy = "user", targetEntity = GameLog::class)
    var gamesLogsList: List<GameLog>,
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
    var usersList: List<User>
)
// Game Log Entity
@Entity
@Table(name = "game_log")
data class GameLog(
    // Game Log Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var createdDate: ZonedDateTime,
    var finished: ZonedDateTime?,
    var platinum: ZonedDateTime?,
    // Game Log Relationships
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false, referencedColumnName = "slug")
    var game: Game,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "email")
    var user: User,
    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false, referencedColumnName = "id")
    var platform: Platform
)
// Game Entity
@Entity
@Table(name = "game")
data class Game(
    // Game Properties
    @Id
    var slug: String,
    var name: String,
    var rating: Float,
    var metacritic: Int,
    var releaseDate: String,
    var imageUrl: String,
    // Game Relationships
    @OneToMany(mappedBy = "game", targetEntity = GameLog::class)
    var gamesLogsList: List<GameLog>,
    @ManyToMany(targetEntity = Platform::class)
    @JoinTable(
        name = "game_platform",
        joinColumns = [JoinColumn(name = "game_slug", referencedColumnName = "slug")],
        inverseJoinColumns = [JoinColumn(name = "platform_id", referencedColumnName = "id")]
    )
    var platformsList: Set<Platform>,
    @ManyToMany(targetEntity = Genre::class)
    @JoinTable(
        name = "game_genre",
        joinColumns = [JoinColumn(name = "game_slug", referencedColumnName = "slug")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    var genresList: Set<Genre>
)
// Platform Entity
@Entity
@Table(name = "platforms")
data class Platform(
    // Platform Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    // Platform Relationships
    @OneToMany(mappedBy = "platform", targetEntity = GameLog::class)
    var gamesLogsList: List<GameLog>,
    @ManyToMany(mappedBy = "platformsList", fetch = FetchType.LAZY, targetEntity = Game::class)
    var gamesList: Set<Game>
)
// Game Genre Entity
@Entity
@Table(name = "genres")
data class Genre(
    // Genre Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    // Genre Relationships
    @ManyToMany(mappedBy = "genresList", fetch = FetchType.LAZY, targetEntity = Game::class)
    var gamesList: Set<Game>
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
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "email")
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