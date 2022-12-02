package no.nav.familie.ef.proxy.sigrun

import no.nav.familie.http.client.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class SigrunClient(
    @Value("\${SIGRUN_URL}") private val uri: URI,
    @Qualifier("azure") restOperations: RestOperations
) : AbstractRestClient(restOperations, "sigrun") {

    fun hentBeregnetSkatt(personIdent: String, inntektsår: Int): Map<String, Any> {
        val uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).pathSegment("beregnetskatt")
            .queryParam("inntektsaar", inntektsår)

        return postForEntity(uriComponentsBuilder.build().toUri(), mapOf("fnr" to personIdent))
    }

    fun hentSummertSkattegrunnlag(personIdent: String, inntektsår: Int): Map<String, Any> {
        val uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).pathSegment("summertskattegrunnlag")
            .queryParam("inntektsaar", inntektsår)

        return postForEntity(uriComponentsBuilder.build().toUri(), mapOf("fnr" to personIdent))
    }
}
