package no.nav.familie.ef.proxy.arbeidssøker

import no.nav.familie.http.client.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.LocalDate

@Service
class ArbeidssøkerClient(@Value("\${ARBEIDSSØKER_URL}")
                         private val uri: URI,
                         @Qualifier("azure") restOperations: RestOperations)
    : AbstractRestClient(restOperations, "pdl.personinfo.saksbehandler") {

    fun hentPerioder(personIdent: String, fraOgMed: LocalDate, tilOgMed: LocalDate?): Map<String, String> {
        val uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).path("arbeidssoker")
                .queryParam("fraOgMed", fraOgMed)
        tilOgMed?.let { uriComponentsBuilder.queryParam("tilOgMed", tilOgMed) }

        return postForEntity(uriComponentsBuilder.build().toUri(), mapOf("fnr" to personIdent))
    }

}
