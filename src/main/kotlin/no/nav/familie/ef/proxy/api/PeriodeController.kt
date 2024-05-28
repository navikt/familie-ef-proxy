package no.nav.familie.ef.proxy.api

import no.nav.familie.ef.proxy.integration.SakClient
import no.nav.familie.ef.proxy.security.StsValidator
import no.nav.familie.kontrakter.felles.ef.EksternePerioderRequest
import no.nav.familie.kontrakter.felles.ef.EksternePerioderResponse
import no.nav.security.token.support.core.api.Protected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["/api/ekstern"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class PeriodeController(
    private val sakClient: SakClient,
    private val stsValidator: StsValidator,
) {
    // PerioderOvergangsstønadRequest er egentlige perioder av alle typer perioder fra EF
    @PostMapping("/periode/overgangsstonad", "/perioder")
    @Protected
    fun hentPerioder(
        @RequestBody request: EksternePerioderRequest,
    ): EksternePerioderResponse {
        stsValidator.validateSts("srvArena")
        return sakClient.post(request, "api/ekstern/perioder")
    }
}
