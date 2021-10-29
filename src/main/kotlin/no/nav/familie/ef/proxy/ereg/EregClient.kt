package no.nav.familie.ef.proxy.ereg

import no.nav.familie.http.client.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class EregClient(
    @Value("\${EREG_URL}") private val uri: URI,
    @Qualifier("sts") restOperations: RestOperations
) : AbstractRestClient(restOperations, "ereg") {


    fun hentOrganisasjoner(organisasjonsnumre: List<String>): List<Organisasjon> {
        return organisasjonsnumre.map {
            Organisasjon(it, getForEntity(UriComponentsBuilder.fromUri(uri).pathSegment(it).build().toUri(), headers()))
        }.toList()
    }

    private fun headers(): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }
    }
}

data class Organisasjon(
    val orgnr : String,
    val response: Map<String, Any>
)