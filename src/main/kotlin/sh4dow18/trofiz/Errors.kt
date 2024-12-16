package sh4dow18.trofiz
// Error Handlers Requirements
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
// "API Error" data class for use in the "Response Entity" constructor
data class ApiError(
    var status: HttpStatus,
    var message: String?
)
// "Element already exists" class based on "Runtime Exception" class for use in error handlers with a template message
class ElementAlreadyExists(element: String, existsHow: String) :
    RuntimeException("El elemento $element ya existe en el sistema como $existsHow")
// "No Such Element Exists" class based on "Runtime Exception" class for use in error handlers with a template message
class NoSuchElementExists(element: String, notExistsHow: String) :
    RuntimeException("El elemento con el identificador $element no existe en el sistema como $notExistsHow")
class NoSuchElementsExists(elements: List<String>, notExistsHow: String) :
    RuntimeException("Los elementos con los identificadores $elements no existen como $notExistsHow")
// "Bad Request" class based on "Runtime Exception" class for use in error handlers with a template message
class BadRequest(message: String) : RuntimeException(message)
// Error Handlers Main Class
@ControllerAdvice
class ErrorsHandler: ResponseEntityExceptionHandler() {
    // Error handler when some element already exists and there cannot be a duplicate of it
    @ExceptionHandler(ElementAlreadyExists::class)
    fun elementAlreadyExists(
        exception: java.lang.Exception,
    ): ResponseEntity<Any> {
        val apiError = ApiError(
            status = HttpStatus.CONFLICT,
            message = exception.message
        )
        logger.debug("Element Already Exists: {}", exception)
        return ResponseEntity(apiError, apiError.status)
    }
    // Error handler when some elements don't exist and needs to exist
    @ExceptionHandler(value = [NoSuchElementExists::class, NoSuchElementsExists::class])
    fun noSuchElementExists(
        exception: java.lang.Exception,
    ): ResponseEntity<Any> {
        val apiError = ApiError(
            status = HttpStatus.NOT_FOUND,
            message = exception.message
        )
        logger.debug("No Such Element Exists: {}", exception)
        return ResponseEntity(apiError, apiError.status)
    }
    // Error handler when some HTTP request is wrong
    @ExceptionHandler(BadRequest::class)
    fun badRequest(
        exception: java.lang.Exception,
    ): ResponseEntity<Any> {
        val apiError = ApiError(
            status = HttpStatus.BAD_REQUEST,
            message = exception.message
        )
        logger.debug("Bad Request: {}", exception)
        return ResponseEntity(apiError, apiError.status)
    }
    // Default error handler for each exception that does not have a specific error handler
    @ExceptionHandler(Exception::class, RuntimeException::class)
    fun default(
        exception: java.lang.Exception,
    ): ResponseEntity<Any> {
        val apiError = ApiError(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = exception.message
        )
        logger.debug("Default Error Handler: {}", exception)
        return ResponseEntity(apiError, apiError.status)
    }
}