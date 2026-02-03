package no.nav.familie.ef.proxy.sigrun

import no.nav.familie.kontrakter.felles.PersonIdent
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.YearMonth

@RestController
@RequestMapping(
    "/api/sigrun",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@ProtectedWithClaims(issuer = "azuread")
@Validated
class SigrunController(
    private val sigrunClient: SigrunClient,
) {
    @PostMapping("/pensjonsgivendeinntekt")
    fun hentPensjonsgivendeInntekt(
        @RequestBody personIdent: PersonIdent,
        @RequestParam("inntektsaar", required = false) inntektsår: Int?,
    ): Map<String, Any> =
        sigrunClient.hentPensjonsgivendeInntekt(
            personIdent.ident,
            inntektsår ?: (YearMonth.now().year - 1),
        )
}
