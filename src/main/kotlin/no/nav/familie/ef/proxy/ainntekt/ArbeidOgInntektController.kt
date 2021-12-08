package no.nav.familie.ef.proxy.ainntekt

import no.nav.familie.kontrakter.felles.PersonIdent
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.Unprotected
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/ainntekt")
@ProtectedWithClaims(issuer = "azuread")
@Validated
class ArbeidOgInntektController(private val client: ArbeidOgInntektClient) {

    /**
     * Brukes for å generere en url til arbeid-og-inntekt
     * for å kunne sende saksbehandleren til identen sin side på arbeid og inntekt
     */
    @PostMapping("link")
    @Unprotected // Denne er ikke beskyttet i a-inntekt
    fun hentUrlTilArbeidOgInntekt(@RequestBody request: PersonIdent): URI {
        return client.hentUrlTilArbeidOgInntekt(request.ident)
    }

}