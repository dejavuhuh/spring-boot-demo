package kiss.web

import org.babyfish.jimmer.error.CodeBasedRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(ex: Exception): ProblemDetail {
        logger.error("Unexpected exception", ex)
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CodeBasedRuntimeException::class)
    fun handleJimmerException(ex: CodeBasedRuntimeException): JimmerError {
        return JimmerError(ex.family, ex.code)
    }

    data class JimmerError(val family: String, val code: String)
}