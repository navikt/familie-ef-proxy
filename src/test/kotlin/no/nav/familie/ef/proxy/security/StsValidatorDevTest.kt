package no.nav.familie.ef.proxy.security

import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.PlainJWT
import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import no.nav.security.token.support.core.exceptions.JwtTokenInvalidClaimException
import no.nav.security.token.support.core.exceptions.JwtTokenMissingException
import no.nav.security.token.support.core.jwt.JwtToken
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap

internal class StsValidatorDevTest {

    private lateinit var contextHolder: TokenValidationContextHolderImpl
    private lateinit var stsValidator: StsValidatorDev

    @BeforeEach
    internal fun setUp() {
        contextHolder = createContextHolder(TokenValidationContext(emptyMap()))
        stsValidator = StsValidatorDev(contextHolder)
    }

    @Test
    internal fun `savner token`() {
        assertThat(catchThrowable { stsValidator.validateSts("sts") })
            .isInstanceOf(JwtTokenMissingException::class.java)
            .hasMessage("Savner gyldig issuer")
    }

    @Test
    internal fun `har token med feil issuer`() {
        setupStsValidatorWithTokenValidationContext("azuread", "annetSubject")
        assertThat(catchThrowable { stsValidator.validateSts("subject") })
            .isInstanceOf(JwtTokenMissingException::class.java)
            .hasMessage("Savner gyldig issuer")
    }

    @Test
    internal fun `har token med feil subject`() {
        setupStsValidatorWithTokenValidationContext("sts", "annetSubject")
        assertThat(catchThrowable { stsValidator.validateSts("subject") })
            .isInstanceOf(JwtTokenInvalidClaimException::class.java)
            .hasMessage("Subject validerer ikke")
    }

    @Test
    internal fun `validerer sts med riktig subject`() {
        setupStsValidatorWithTokenValidationContext("sts", "subject")
        stsValidator.validateSts("subject")
    }

    @Test
    internal fun `validerer stsTest med riktig subject`() {
        setupStsValidatorWithTokenValidationContext("stsTest", "subject")
        stsValidator.validateSts("subject")
    }

    private fun createOidcValidationContext(issuerShortName: String, jwtToken: JwtToken): TokenValidationContext {
        val map: MutableMap<String, JwtToken> = ConcurrentHashMap()
        map[issuerShortName] = jwtToken
        return TokenValidationContext(map)
    }

    private fun setupStsValidatorWithTokenValidationContext(issuerName: String, subject: String) {
        val claims: JwtToken = createJwtToken(
            "aclaim",
            "value",
            subject,
        )
        val context = createOidcValidationContext(issuerName, claims)
        contextHolder = createContextHolder(context)
        stsValidator = StsValidatorDev(contextHolder)
    }

    private fun createJwtToken(claimName: String, claimValue: String, subject: String): JwtToken {
        val jwt: JWT = PlainJWT(
            JWTClaimsSet.Builder()
                .subject(subject)
                .issuer("http//issuer1")
                .claim(claimName, claimValue).build(),
        )
        return JwtToken(jwt.serialize())
    }

    private fun createContextHolder(tokenValidationContext: TokenValidationContext): TokenValidationContextHolderImpl {
        return TokenValidationContextHolderImpl(tokenValidationContext)
    }
}

data class TokenValidationContextHolderImpl(var validationContext: TokenValidationContext?) : TokenValidationContextHolder {

    override fun getTokenValidationContext(): TokenValidationContext {
        return validationContext!!
    }

    override fun setTokenValidationContext(tokenValidationContext: TokenValidationContext?) {
        validationContext = tokenValidationContext
    }
}
