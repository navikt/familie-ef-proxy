package no.nav.familie.ef.proxy.integration

import no.nav.familie.http.client.AbstractPingableRestClient
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

    fun post(data: Any, path: String): Any {
        val uri = UriComponentsBuilder.fromUri(uri).pathSegment(path).build().toUri()
        return postForEntity(uri, data)
    }

    override val pingUri: URI = UriComponentsBuilder.fromUri(uri).pathSegment("api/ping").build().toUri()

}