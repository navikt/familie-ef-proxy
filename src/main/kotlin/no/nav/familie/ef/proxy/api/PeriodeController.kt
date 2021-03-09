package no.nav.familie.ef.proxy.api

import no.nav.familie.ef.proxy.integration.SakClient
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/ekstern/periode/overgangsstonad"],
                consumes = [MediaType.APPLICATION_JSON_VALUE],
                produces = [MediaType.APPLICATION_JSON_VALUE])
class PeriodeController(private val sakClient: SakClient) {

    @PostMapping
    @ProtectedWithClaims(issuer = "sts", claimMap = ["sub=srvArena"])
    fun hentPerioder(@RequestBody request: Any): Any {
        return sakClient.post(request, "api/ekstern/periode/overgangsstonad/azure")
    }

    // TODO remove
    @PostMapping("azure")
    @ProtectedWithClaims(issuer = "azuread")
    fun hentPerioderAzure(@RequestBody request: Any): Any {
        return sakClient.post(request, "api/ekstern/periode/overgangsstonad/azure")
    }

}