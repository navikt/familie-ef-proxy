package no.nav.familie.ef.proxy.security

import no.nav.security.token.support.core.context.TokenValidationContextHolder
import no.nav.security.token.support.core.exceptions.JwtTokenMissingException
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Gjør det mulig å validere token utstedt fra både sts i Q och T(stsTest)
 */
@Profile("dev", "local")
@Component
class StsValidatorDev(private val tokenValidationContextHolder: TokenValidationContextHolder) : StsValidator {

    override fun validateSts(subject: String) {
        val context = tokenValidationContextHolder.getTokenValidationContext()
            ?: error("tokenValidationContext cannot be null, check your configuration")
        if (context.validateIssuerWithSubject("sts", subject) || context.validateIssuerWithSubject("stsTest", subject)) {
            return
        }
        throw JwtTokenMissingException("Savner gyldig issuer")
    }
}
