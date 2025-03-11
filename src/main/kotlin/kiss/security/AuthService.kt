package kiss.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kiss.verification.*
import org.babyfish.jimmer.sql.exception.SaveException
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthService(
    val verificationCodeStorage: VerificationCodeStorage,
    val sql: KSqlClient,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager
) {

    @PostMapping("/sign-up")
    @Throws(
        VerificationCodeException::class,
        AuthException.AccountAlreadyExists::class
    )
    fun signUp(@RequestBody request: SignUpRequest) {
        val (accountType, account, verificationCode, password) = request

        // 校验验证码
        verificationCodeStorage.verify(
            VerificationCodeIdentifier(Purpose.SIGN_UP, accountType, account),
            verificationCode
        )

        // 创建账号
        try {
            sql.insert(Account {
                when (accountType) {
                    ReceiverType.EMAIL -> email = account
                    ReceiverType.PHONE -> phone = account
                }
                this.password = passwordEncoder.encode(password)
            })
        } catch (ex: SaveException.NotUnique) {
            throw AuthException.accountAlreadyExists(ex.message, ex)
        }
    }

    private val securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
    private val securityContextRepository = HttpSessionSecurityContextRepository()

    @PostMapping("/sign-in")
    @Throws(AuthException.AccountOrPasswordMismatch::class)
    fun signIn(
        @RequestBody signInRequest: SignInRequest,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val token = UsernamePasswordAuthenticationToken.unauthenticated(signInRequest.account, signInRequest.password)
        try {
            val authentication = authenticationManager.authenticate(token)
            val context = securityContextHolderStrategy.createEmptyContext()
            context.authentication = authentication
            securityContextHolderStrategy.context = context
            securityContextRepository.saveContext(context, request, response)
        } catch (ex: BadCredentialsException) {
            throw AuthException.accountOrPasswordMismatch(ex.message, ex)
        }
    }
}
