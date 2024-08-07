package no.nav.familie.ef.proxy.api

import no.nav.familie.ef.proxy.integration.InntektClient
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
    "/api/inntekt",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@ProtectedWithClaims(issuer = "azuread")
@Validated
class InntektController(
    private val inntektClient: InntektClient,
) {
    @PostMapping
    fun hentInntekt(
        @RequestBody request: PersonIdent,
        @RequestParam("fom", required = false) fom: YearMonth?,
        @RequestParam("tom", required = false) tom: YearMonth?,
    ): Map<String, Any> =
        inntektClient.hentInntekt(
            personIdent = request.ident,
            fom = fom ?: YearMonth.now().minusMonths(2),
            tom = tom ?: YearMonth.now(),
        )

    @PostMapping("/historikk")
    fun hentInntektshistorikk(
        @RequestBody request: PersonIdent,
        @RequestParam("fom", required = false) fom: YearMonth?,
        @RequestParam("tom", required = false) tom: YearMonth?,
    ): Map<String, Any> =
        inntektClient.hentInntektshistorikk(
            personIdent = request.ident,
            fom = fom ?: YearMonth.now().minusMonths(12),
            tom = tom ?: YearMonth.now(),
        )
}
