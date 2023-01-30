package no.nav.familie.ef.proxy.ereg

import no.nav.familie.http.client.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class EregClient(
    @Value("\${EREG_URL}") private val uri: URI,
    @Qualifier("sts") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "ereg") {

    fun hentOrganisasjoner(organisasjonsnumre: List<String>): List<Map<String, Any>> {
        val organisasjoner = mutableListOf<Map<String, Any>>()

        organisasjonsnumre.forEach {
            organisasjoner.add(getForEntity(UriComponentsBuilder.fromUri(uri).pathSegment(it).build().toUri()))
        }
        return organisasjoner
    }
}
