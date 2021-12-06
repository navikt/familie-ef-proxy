package no.nav.familie.ef.proxy.arbeidssøker

import no.nav.familie.kontrakter.felles.PersonIdent
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/arbeidssoker",
                consumes = [MediaType.APPLICATION_JSON_VALUE],
                produces = [MediaType.APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = "azuread")
@Validated
class ArbeidssøkerController(private val client: ArbeidssøkerClient) {

    @PostMapping("perioder")
    fun hentPerioder(@RequestBody request: PersonIdent,
                     @RequestParam fraOgMed: LocalDate,
                     @RequestParam tilOgMed: LocalDate? = null): Map<String, Any> {
        return client.hentPerioder(request.ident, fraOgMed, tilOgMed)
    }

}
