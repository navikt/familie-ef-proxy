package no.nav.familie.ef.proxy.api

import no.nav.familie.ef.proxy.integration.FamilieIntegrasjonerClient
import no.nav.familie.ef.proxy.security.StsValidator
import no.nav.familie.kontrakter.felles.ef.PerioderOvergangsstønadRequest
import no.nav.familie.kontrakter.felles.ef.PerioderOvergangsstønadResponse
import no.nav.security.token.support.core.api.Protected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/ekstern/periode/overgangsstonad"],
                consumes = [MediaType.APPLICATION_JSON_VALUE],
                produces = [MediaType.APPLICATION_JSON_VALUE])
class PeriodeController(private val familieIntegrasjonerClient: FamilieIntegrasjonerClient,
                        //private val sakClient: SakClient, // TODO skal hente perioder fra ef-sak når er-sak er oppe og kjører
                        private val stsValidator: StsValidator) {

    @PostMapping
    @Protected
    fun hentPerioder(@RequestBody request: PerioderOvergangsstønadRequest): PerioderOvergangsstønadResponse {
        stsValidator.validateSts("srvArena")
        //return sakClient.post(request, "api/ekstern/periode/overgangsstonad/azure")
        return familieIntegrasjonerClient.hentInfotrygdPerioder(request)
    }

}