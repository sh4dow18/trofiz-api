package sh4dow18.trofiz
// Services Requirements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
// Platform Service Interface where the functions to be used in
// Spring Abstract Platform Service are declared
interface PlatformService {
    fun findAll(): List<PlatformResponse>
    fun insert(platformRequest: PlatformRequest): PlatformResponse
}
// Spring Abstract Platform Service
@Service
class AbstractPlatformService(
    // Platform Service Props
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val platformMapper: PlatformMapper
): PlatformService {
    override fun findAll(): List<PlatformResponse> {
        // Returns all Platforms as a Platform Responses List
        return platformMapper.platformsListToPlatformResponsesList(platformRepository.findAll())
    }
    // @Transactional is a Tag that establishes that is a Transactional Service Function. This
    // one makes a transaction when this service function is in operation.
    @Transactional(rollbackFor = [ElementAlreadyExists::class])
    override fun insert(platformRequest: PlatformRequest): PlatformResponse {
        // Transforms Name in Platform Request in lowercase and replace spaces with "-"
        // Example: "Play Station 5" -> "play-station-5"
        val platformId = getIdByName(platformRequest.name)
        // Verifies if the platform already exists
        if (platformRepository.findById(platformId).orElse(null) != null) {
            throw ElementAlreadyExists(platformRequest.name, "Plataforma" )
        }
        // If not exists, create the new platform
        val newPlatform = platformMapper.platformRequestToPlatform(platformRequest)
        // Save new Platform and Returns it as Platform Response
        return platformMapper.platformToPlatformResponse(platformRepository.save(newPlatform))
    }
}
// Genre Service Interface where the functions to be used in
// Spring Abstract Genre Service are declared
interface GenreService {
    fun findAll(): List<GenreResponse>
    fun insert(genreRequest: GenreRequest): GenreResponse
}
// Spring Abstract Genre Service
@Service
class AbstractGenreService(
    // Genre Service Props
    @Autowired
    val genreRepository: GenreRepository,
    @Autowired
    val genreMapper: GenreMapper
): GenreService {
    override fun findAll(): List<GenreResponse> {
        // Returns all Genres as a Genre Responses List
        return genreMapper.genresListToGenreResponsesList(genreRepository.findAll())
    }
    override fun insert(genreRequest: GenreRequest): GenreResponse {
        // Transforms Name in Genre Request in lowercase and replace spaces with "-"
        // Example: "Interactive Adventure" -> "interactive-adventure"
        val genreId = getIdByName(genreRequest.name)
        // Verifies if the genre already exists
        if (genreRepository.findById(genreId).orElse(null) != null) {
            throw ElementAlreadyExists(genreRequest.name, "Género" )
        }
        // If not exists, create the new genre
        val newGenre = genreMapper.genreRequestToGenre(genreRequest)
        // Transforms the New Genre to Genre Response and Returns It
        return genreMapper.genreToGenreResponse(genreRepository.save(newGenre))
    }
}
// Game Service Interface where the functions to be used in
// Spring Abstract Game Service are declared
interface GameService {
    fun findAll(): List<GameResponse>
    fun findTop10ByNameContainingIgnoreCase(name: String): List<GameResponse>
    fun findById(id: String): GameResponse
    fun insert(gameRequest: GameRequest): GameResponse
}
// Spring Abstract Game Service
@Service
class AbstractGameService(
    // Game Service Props
    @Autowired
    val gameRepository: GameRepository,
    @Autowired
    val gameMapper: GameMapper,
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val genreRepository: GenreRepository
): GameService {
    override fun findAll(): List<GameResponse> {
        // Transforms a Games List to a Game Responses List
        return gameMapper.gamesListToGameResponsesList(gameRepository.findAll())
    }
    override fun findTop10ByNameContainingIgnoreCase(name: String): List<GameResponse> {
        // Transforms the first 10 Games from a Games List to a Game Responses List
        return gameMapper.gamesListToGameResponsesList(gameRepository.findTop10ByNameContainingIgnoreCase(name))
    }
    override fun findById(id: String): GameResponse {
        // Find a game with the id and if the game is not found, throw a "No Such Element Exists" error
        val game = gameRepository.findById(id).orElseThrow {
            NoSuchElementExists(id, "Juego")
        }
        // Transforms a Game to a Game Response
        return gameMapper.gameToGameResponse(game)
    }
    override fun insert(gameRequest: GameRequest): GameResponse {
        // Verifies if the game already exists
        if (gameRepository.findById(getIdByName(gameRequest.name)).orElse(null) != null) {
            throw ElementAlreadyExists(gameRequest.name, "Juego")
        }
        // If not exists, create the new game
        val newGame = gameMapper.gameRequestToGame(gameRequest)
        // Add existing platforms to the new game
        newGame.platformsList = connectEntities(platformRepository, gameRequest.platformsList)
        // Add existing genres to the new game
        newGame.genresList = connectEntities(genreRepository, gameRequest.genresList)
        // Transforms the New Game to a Game Response and Returns it
        return gameMapper.gameToGameResponse(gameRepository.save(newGame))
    }
}
// Privilege Service Interface where the functions to be used in
// Spring Abstract Privilege Service are declared
interface PrivilegeService {
    fun findAll(): List<PrivilegeResponse>
    fun insert(privilegeRequest: PrivilegeRequest): PrivilegeResponse
    fun updateStatus(id: String): PrivilegeResponse
}
// Spring Abstract Game Service
@Service
class AbstractPrivilegeService(
    // Privilege Service Props
    @Autowired
    val privilegeRepository: PrivilegeRepository,
    @Autowired
    val privilegeMapper: PrivilegeMapper
): PrivilegeService {
    override fun findAll(): List<PrivilegeResponse> {
        // Transforms a Privilege List to a Privilege Responses List
        return privilegeMapper.privilegesListToPrivilegeResponsesList(privilegeRepository.findAll())
    }
    override fun insert(privilegeRequest: PrivilegeRequest): PrivilegeResponse {
        // Transforms Name in Privilege Request in lowercase and replace spaces with "-"
        // Example: "Add Games" -> "add-games"
        val privilegeId = getIdByName(privilegeRequest.name)
        // Verifies if the Privilege already exists
        if (privilegeRepository.findById(privilegeId).orElse(null) != null) {
            throw ElementAlreadyExists(privilegeId, "Privilegio")
        }
        // If not exists, create the new Privilege
        val newPrivilege = privilegeMapper.privilegeRequestToPrivilege(privilegeRequest)
        // Transforms the New Privilege to Privilege Response
        return privilegeMapper.privilegeToPrivilegeResponse(privilegeRepository.save(newPrivilege))
    }
    override fun updateStatus(id: String): PrivilegeResponse {
        // Verifies if the Privilege already exists
        val privilege = privilegeRepository.findById(id).orElseThrow {
            NoSuchElementExists(id,"Privilegio")
        }
        // Change enabled from true to false and vice versa
        privilege.enabled = !privilege.enabled
        // Transforms the Privilege to Privilege Response
        return privilegeMapper.privilegeToPrivilegeResponse(privilegeRepository.save(privilege))
    }
}
// Role Service Interface where the functions to be used in
// Spring Abstract Role Service are declared
interface RoleService {
    fun findAll(): List<RoleResponse>
    fun insert(roleRequest: RoleRequest): RoleResponse
    fun update(updateRoleRequest: UpdateRoleRequest): RoleResponse
}
// Spring Abstract Game Service
@Service
class AbstractRoleService(
    // Role Service Props
    @Autowired
    val roleRepository: RoleRepository,
    @Autowired
    val privilegeRepository: PrivilegeRepository,
    @Autowired
    val roleMapper: RoleMapper
): RoleService {
    override fun findAll(): List<RoleResponse> {
        // Transforms a Role List to a Role Responses List
        return roleMapper.rolesListToRoleResponsesList(roleRepository.findAll())
    }
    override fun insert(roleRequest: RoleRequest): RoleResponse {
        // Verifies if the Role already exists
        if (roleRepository.findByNameIgnoringCase(roleRequest.name).orElse(null) != null) {
            throw ElementAlreadyExists(roleRequest.name, "Rol")
        }
        // Check if the privileges on the submitted role already exist
        val privilegesList = privilegeRepository.findAllById(roleRequest.privilegesList)
        if (privilegesList.size != roleRequest.privilegesList.size) {
            val missingIds = roleRequest.privilegesList - privilegesList.map { it.id }.toSet()
            throw NoSuchElementExists(missingIds.toString(), "Privilegios")
        }
        // If each privileges exist, create the new role
        val newRole = roleMapper.roleRequestToRole(roleRequest)
        newRole.privilegesList = privilegeRepository.saveAll(privilegesList).toSet()
        // Transforms the New Role to Role Response
        return roleMapper.roleToRoleResponse(roleRepository.save(newRole))
    }
    override fun update(updateRoleRequest: UpdateRoleRequest): RoleResponse {
        // Verifies if the Role already exists
        val role = roleRepository.findById(updateRoleRequest.id).orElseThrow {
            NoSuchElementExists("${updateRoleRequest.id}", "Rol")
        }
        // Check if the privileges on the submitted role already exist
        val privilegesList = privilegeRepository.findAllById(updateRoleRequest.privilegesList)
        if (privilegesList.size != updateRoleRequest.privilegesList.size) {
            val missingIds = updateRoleRequest.privilegesList - privilegesList.map { it.id }.toSet()
            throw NoSuchElementExists(missingIds.toString(), "Privilegios")
        }
        // If the Role exists and the Privileges Exists, update it
        role.privilegesList = privilegesList.toSet()
        // Transforms the New Role to Role Response
        return roleMapper.roleToRoleResponse(roleRepository.save(role))
    }
}