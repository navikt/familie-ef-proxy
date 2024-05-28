package no.nav.familie.ef.proxy.ainntekt

import no.nav.familie.kontrakter.felles.PersonIdent
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ainntekt")
@ProtectedWithClaims(issuer = "azuread")
@Validated
class ArbeidOgInntektController(private val client: ArbeidOgInntektClient) {
    /**
     * Brukes for å generere en url til arbeid-og-inntekt
     * for å kunne sende saksbehandleren til identen sin side på arbeid og inntekt
     */
    @PostMapping("generer-url")
    fun hentUrlTilArbeidOgInntekt(
        @RequestBody request: PersonIdent,
    ): String {
        return client.hentUrlTilArbeidOgInntekt(request.ident)
    }

    @PostMapping("generer-url-arbeidsforhold")
    fun hentUrlTilArbeidOgInntektArbeidsforhold(
        @RequestBody personIdent: PersonIdent,
    ): String {
        return client.hentUrlTilArbeidsforhold(personIdent.ident)
    }
}
