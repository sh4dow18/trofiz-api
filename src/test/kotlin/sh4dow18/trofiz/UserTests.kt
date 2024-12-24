package sh4dow18.trofiz
// User Tests Requirements
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.transaction.annotation.Transactional
import java.awt.Color
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
// User Test Main Class
@SpringBootTest
class UserTests(
    // User Tests Props
    @Value("\${files.path}")
    val filesPath: String,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val userMapper: UserMapper,
    @Autowired
    val roleRepository: RoleRepository,
    @Autowired
    val reviewMapper: ReviewMapper,
    @Autowired
    val logMapper: LogMapper
) {
    @Test
    fun findAll() {
        // Transforms a User List to a User Responses List
        userMapper.usersListToUserResponsesList(userRepository.findAll())
    }
    @Test
    // Makes it transactional to use Review in User
    @Transactional
    fun findAllReviewsById() {
        // Find All Reviews by id Props
        val id = 1L
        // Check if the user already exists
        val user = userRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Usuario")
        }
        // Transforms a Reviews List to a Review Responses List
        reviewMapper.reviewsListToReviewResponsesList(user.reviewsList)
    }
    @Test
    // Makes it transactional to use Log in User
    @Transactional
    fun findAllLogsById() {
        // Find All Reviews by id Props
        val id = 1L
        // Check if the user already exists
        val user = userRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Usuario")
        }
        // Transforms a Logs List to a Log Responses List
        logMapper.logsListToLogsResponsesList(user.logsList)
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
        val user = userRepository.findByEmailOrName(userRequest.email, userRequest.name).orElse(null)
        if (user != null) {
            val prop = if (user.email == userRequest.email) user.email else user.name
            throw ElementAlreadyExists(prop!!, "Usuario")
        }
        // Check if the username length is less than 16 characters
        if (userRequest.name.length > 16) {
            throw BadRequest("El nombre de usuario debe ser menor a 16 caracteres")
        }
        // Check if the "Gamer" role already exist
        val role = roleRepository.findByNameIgnoringCase("Gamer").orElseThrow {
            NoSuchElementExists("Gamer", "Rol")
        }
        // If the role exist, create the new User
        val newUser = userMapper.userRequestToUser(userRequest, role)
        // Transforms the New User to User Response
        userMapper.userToUserResponse(newUser)
    }
    @Test
    fun update() {
        // Update User Test Prop
        val updateUserRequest = UpdateUserRequest(1, "Ramsés Solano")
        // Update User Image Test Prop
        // Generate a blank image to test
        val width = 500
        val height = 400
        val blankImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphicsExample = blankImage.createGraphics()
        graphicsExample.color = Color.WHITE
        graphicsExample.fillRect(0, 0, width, height)
        graphicsExample.dispose()
        // Transform the image into bytes
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(blankImage, "jpg", outputStream)
        val imageBytes = outputStream.toByteArray()
        // Create a Fake multipart file to test
        val image = MockMultipartFile("file", "test-image.jpg", "image/jpeg", imageBytes)
        // Verifies if the User already exists
        val user = userRepository.findById(updateUserRequest.id).orElseThrow {
            NoSuchElementExists("${updateUserRequest.id}", "Usuario")
        }
        if (updateUserRequest.name != null) {
            // Check if the username length is less than 16 characters
            if (updateUserRequest.name!!.length > 16) {
                throw BadRequest("El nombre de usuario debe ser menor a 16 caracteres")
            }
            val anotherUser = userRepository.findByEmailOrName("", updateUserRequest.name!!).orElse(null)
            if (anotherUser != null) {
                throw ElementAlreadyExists(updateUserRequest.name!!, "Usuario")
            }
            // Update the user
            user.name = updateUserRequest.name!!
        }
        // Verifies if the image file is really an Image
        if (ImageIO.read(image.inputStream) == null) {
            throw BadRequest("Image file type not supported")
        }
        // Create a New Image 150 x 150 from the original image
        val originalImage = ImageIO.read(image.inputStream)
        val targetWidth = 150
        val targetHeight = 150
        val xScale = targetWidth.toDouble() / originalImage.width
        val yScale = targetHeight.toDouble() / originalImage.height
        val scale = if (xScale > yScale) xScale else yScale
        val newWidth = (originalImage.width * scale).toInt()
        val newHeight = (originalImage.height * scale).toInt()
        val resizedImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = resizedImage.createGraphics()
        graphics.clip = Ellipse2D.Float(0f, 0f, targetWidth.toFloat(), targetHeight.toFloat())
        graphics.drawImage(originalImage, (targetWidth - newWidth) / 2, (targetHeight - newHeight) / 2, newWidth, newHeight, null)
        graphics.dispose()
        user.image = true
        // Transforms the User to User Response
        userMapper.userToUserResponse(user)
    }
    @Test
    fun changeRole() {
        // Change Role Test Prop
        val changeRoleUserRequest = ChangeRoleUserRequest(1, 2, 1)
        // Verifies if the User already exists
        val user = userRepository.findById(changeRoleUserRequest.userId).orElseThrow {
            NoSuchElementExists("${changeRoleUserRequest.userId}", "Usuario")
        }
        // Verifies if the Role already exists
        val role = roleRepository.findById(changeRoleUserRequest.roleId).orElseThrow {
            NoSuchElementExists("${changeRoleUserRequest.roleId}", "Rol")
        }
        // If the user and role were found, update the user
        user.role = role
        // Transforms the User to User Response
        userMapper.userToUserResponse(user)
    }
    @Test
    fun closeAccount() {
        // Close Account Test Prop
        val id = 1L
        // Verifies if the User already exists
        val user = userRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Usuario")
        }
        // Update user
        // Check if the user has a profile image, if exists, delete it
        if (user.image) {
            val image = File("$filesPath/users/${user.email}.png")
            val imageDelete = image.delete()
            if (imageDelete) {
                user.image = false
            }
        }
        // Delete all the user's personal information
        user.email = null
        user.name = null
        user.password = null
        user.enabled = false
        // Transforms the User to User Response
        userMapper.userToUserResponse(user)
    }
}