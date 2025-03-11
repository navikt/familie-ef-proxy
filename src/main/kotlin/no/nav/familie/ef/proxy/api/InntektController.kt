package no.nav.familie.ef.proxy.api

import no.nav.familie.ef.proxy.integration.InntektClient
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
    @PostMapping("v2")
    fun hentInntekt(
        @RequestBody inntektV2Request: InntektV2Request,
    ): Map<String, Any> =
        inntektClient.hentInntekt(
            personIdent = inntektV2Request.personIdent,
            månedFom = inntektV2Request.månedFom ?: YearMonth.now().minusMonths(2),
            månedTom = inntektV2Request.månedTom ?: YearMonth.now(),
        )
}

data class InntektV2Request(
    val personIdent: String,
    val månedFom: YearMonth?,
    val månedTom: YearMonth?,
)
