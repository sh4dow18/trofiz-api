package sh4dow18.trofiz
// User Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
// User Test Main Class
@SpringBootTest
class UserTests(
    // User Tests Props
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val userMapper: UserMapper,
    @Autowired
    val roleRepository: RoleRepository
) {
    @Test
    fun findAll() {
        // Transforms a User List to a User Responses List
        userMapper.usersListToUserResponsesList(userRepository.findAll())
    }
    @Test
    fun findById() {
        // Find User By Id Test Prop
        val userId = 1L
        // Verifies if the User already exists
        val user = userRepository.findById(userId).orElseThrow {
            NoSuchElementExists("$userId", "Usuario")
        }
        // If exists, transforms it to User Response
        userMapper.userToUserResponse(user)
    }
    @Test
    // Makes it transactional to use Role Repository
    @Transactional
    fun insert() {
        // Insert User Test Prop
        val userRequest = UserRequest("sh4dow18@gmail.com", "sh4dow18", "PASSWORD")
        // Verifies if the User already exists
        val user = userRepository.findByEmailOrUserName(userRequest.email, userRequest.userName).orElse(null)
        if (user != null) {
            val prop = if (user.email == userRequest.email) user.email else user.userName
            throw ElementAlreadyExists(prop, "Usuario")
        }
        // Check if the role with id 1 already exist
        val role = roleRepository.findById(1).orElseThrow {
            NoSuchElementExists("${1}", "Rol")
        }
        // If the role exist, create the new User
        val newUser = userMapper.userRequestToUser(userRequest, role)
        // Transforms the New User to User Response
        userMapper.userToUserResponse(newUser)
    }
}