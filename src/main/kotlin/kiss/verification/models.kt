package kiss.verification

import org.babyfish.jimmer.error.ErrorFamily

enum class Purpose { SIGN_UP }
enum class ReceiverType { EMAIL, PHONE }

data class VerificationCodeIdentifier(
    val purpose: Purpose,
    val receiverType: ReceiverType,
    val receiver: String
)

@ErrorFamily
enum class VerificationCodeError {
    EXPIRED,
    MISMATCH
}
