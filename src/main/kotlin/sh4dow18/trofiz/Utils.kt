package sh4dow18.trofiz
// Utils Requirements
import org.slf4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.memberProperties
// Get Platform Id using its own name
// Example: "Play Station 5" -> "play-station-5"
fun getIdByName(name: String): String {
    return name.lowercase().replace(" ", "-")
}
// Get the Current Date in Costa Rica
fun getCurrentDate(): ZonedDateTime {
    return ZonedDateTime.now(ZoneId.of("America/Costa_Rica"))
}
// Function to get a date submitted as a String
fun getDateAsString(date: ZonedDateTime?): String? {
    if (date == null) {
        return null
    }
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("America/Costa_Rica")))
}
// Function to get a String submitted as a Date
fun getStringAsDate(date: String): ZonedDateTime {
    val newDate = date.split(" ")
    val localDateTime = LocalDateTime.of(
        LocalDate.parse(newDate[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        LocalTime.parse(newDate[1], DateTimeFormatter.ofPattern("HH:mm"))
    )
    return ZonedDateTime.of(localDateTime, ZoneId.of("America/Costa_Rica"))
}
// Add Log Function
fun addLog(logService: LogService, action: String, actionType: String, logger: Logger) {
    try {
        logService.insert(LogRequest(action, actionType))
    }
    catch (ex: Exception) {
        logger.error("Error al insertar en el log '$action'", ex)
    }
}
// To Non-Null String returns a string with props that are not null
inline fun <reified T : Any> T.toNonNullString(): String {
    return T::class.memberProperties
        .mapNotNull { prop ->
            val value = prop.get(this)
            if (value != null) "${prop.name}=$value" else null
        }
        .joinToString(", ", "{", "}")
}
// Check if the submitted user could do the submitted action
fun checkUserValidation(userRepository: UserRepository, privilegeId: String) {
    // Check if the user submitted already exists
    val userId = LoggedUser.get()
    val user = userRepository.findById(userId).orElseThrow {
        NoSuchElementExists("$userId", "Usuario")
    }
    // Check if the submitted user has the permissions to perform the submitted action
    if (user.role.privilegesList.none { it.id == privilegeId }) {
        throw Unauthorized()
    }
    // Check if the user is enabled
    if (!user.enabled) {
        throw BadRequest("El Usuario Actual tiene la Cuenta Cerrada")
    }
}
// Function that returns a password encoded with BCrypt Encoder
@Bean
fun encodePassword(password: String): String {
    return BCryptPasswordEncoder().encode(password)
}
