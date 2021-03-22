package no.nav.familie.ef.proxy.integration

import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import no.nav.familie.kontrakter.felles.ef.PerioderOvergangsstønadResponse
import no.nav.familie.kontrakter.felles.getDataOrThrow
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class SakClient(@Qualifier("azure") restOperations: RestOperations,
                @Value("\${EF_SAK_URL}") private val uri: URI)
    : AbstractPingableRestClient(restOperations, "familie.sak") {

    fun post(data: Any, path: String): PerioderOvergangsstønadResponse {
        val uri = UriComponentsBuilder.fromUri(uri).pathSegment(path).build().toUri()
        val postForEntity = postForEntity<Ressurs<PerioderOvergangsstønadResponse>>(uri, data)
        return postForEntity.getDataOrThrow()
    }

    override val pingUri: URI = UriComponentsBuilder.fromUri(uri).pathSegment("api/ping").build().toUri()

}