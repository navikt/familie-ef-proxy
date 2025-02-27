package no.nav.familie.ef.proxy.api

import no.nav.familie.ef.proxy.integration.InntektClient
import no.nav.familie.kontrakter.felles.PersonIdent
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
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

    @RestController
    @RequestMapping(
        "/api/inntektv2",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @ProtectedWithClaims(issuer = "azuread")
    @Validated
    class InntektController(
        private val inntektClient: InntektClient,
    ) {
        @PostMapping
        fun hentInntektV2(
            @RequestBody request: InntektV2Request,
        ): Map<String, Any> {
            val inntekt =
                inntektClient.hentInntektV2(
                    personident = request.personident,
                    maanedFom = request.maanedFom ?: YearMonth.now().minusMonths(2),
                    maanedTom = request.maanedTom ?: YearMonth.now(),
                )

            // TODO: Husk å fjern meg.
            val logger = LoggerFactory.getLogger(::javaClass.name)
            logger.info("FAMILIE-EF-PROXY --- Henter inntekt for personident med data: $inntekt")

            return inntekt
        }

        // TODO Flytt?
        data class InntektV2Request(
            val personident: String,
            val maanedFom: YearMonth?,
            val maanedTom: YearMonth?,
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
}
