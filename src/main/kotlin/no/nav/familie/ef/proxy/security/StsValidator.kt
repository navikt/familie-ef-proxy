package no.nav.familie.ef.proxy.security

import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.core.exceptions.JwtTokenInvalidClaimException

interface StsValidator {

    fun validateSts(subject: String)
}

fun TokenValidationContext.validateIssuerWithSubject(issuerName: String, subject: String): Boolean {
    val sts = try {
        this.getClaims(issuerName)
    } catch (e: IllegalArgumentException) {
        return false
    }

    if (sts.subject == subject) {
        return true
    } else {
        throw JwtTokenInvalidClaimException("Subject validerer ikke")
    }
}
