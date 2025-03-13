package kiss.security

import kiss.verification.ReceiverType
import org.babyfish.jimmer.error.ErrorFamily

data class SignUpRequest(
    val accountType: ReceiverType,
    val account: String,
    val verificationCode: String,
    val password: String,
)

data class SignInRequest(
    val account: String,
    val password: String,
)

@ErrorFamily
enum class AuthError {
    ACCOUNT_ALREADY_EXISTS,
    ACCOUNT_OR_PASSWORD_MISMATCH,
}

@ErrorFamily
enum class RoleError {
    ROLE_ALREADY_EXISTS,
}
