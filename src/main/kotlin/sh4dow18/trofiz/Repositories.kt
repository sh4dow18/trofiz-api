package sh4dow18.trofiz
// JPA Repositories Requirements
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
// Platform Repository
@Repository
interface PlatformRepository: JpaRepository<Platform, String>
// Genre Repository
@Repository
interface GenreRepository: JpaRepository<Genre, String>
// Game Repository
@Repository
interface GameRepository: JpaRepository<Game, String> {
    // Find the first 10 games that start with the submitted name
    fun findTop10ByNameContainingIgnoreCase(@Param("name") name: String): List<Game>
}
// Privilege Repository
@Repository
interface PrivilegeRepository: JpaRepository<Privilege, String>
// Role Repository
@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByNameIgnoringCase(@Param("name") name: String): Optional<Role>
}
// User Repository
@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByEmailOrName(@Param("email") email: String, @Param("name") name: String): Optional<User>
}
// Game Log Repository
@Repository
interface GameLogRepository: JpaRepository<GameLog, Long> {
    fun findByUserIdOrderByCreatedDateAsc(@Param("id") id: Long): List<GameLog>
}
// Review Repository
@Repository
interface ReviewRepository: JpaRepository<Review, Long>