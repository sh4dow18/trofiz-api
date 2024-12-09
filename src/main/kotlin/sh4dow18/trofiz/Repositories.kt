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
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByNameIgnoringCase(@Param("name") name: String): Optional<Role>
}