package sh4dow18.trofiz
// JPA Repositories Requirements
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
// Platform Repository
@Repository
interface PlatformRepository: JpaRepository<Platform, String>