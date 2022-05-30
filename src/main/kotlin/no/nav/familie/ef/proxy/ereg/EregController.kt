package no.nav.familie.ef.proxy.ereg

import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    "/api/ereg",
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@ProtectedWithClaims(issuer = "azuread")
@Validated
class EregController(private val eregClient: EregClient) {

    @GetMapping
    fun hentOrganisasjon(@RequestParam organisasjonsnumre: List<String>): List<Map<String, Any>> {
        return eregClient.hentOrganisasjoner(organisasjonsnumre)
    }
}
