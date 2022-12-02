package no.nav.familie.ef.proxy.sigrun

import no.nav.familie.http.client.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
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

        val headers = HttpHeaders()
        headers.set("x-filter", "BeregnetSkattPensjonsgivendeInntekt")
        headers.set("x-aktoerid", personIdent)
        headers.set("x-inntektsaar", inntektsår.toString())
        return postForEntity(uriComponentsBuilder.build().toUri(), mapOf("fnr" to personIdent), headers)
    }

    fun hentSummertSkattegrunnlag(personIdent: String, inntektsår: Int): Map<String, Any> {
        val uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).pathSegment("summertskattegrunnlag")
            .queryParam("inntektsaar", inntektsår)
            .queryParam("inntektsfilter", "SummertSkattegrunnlagEnsligForsorger")

        val headers = HttpHeaders()
        headers.set("Nav-Personident", personIdent)

        return postForEntity(uriComponentsBuilder.build().toUri(), mapOf("fnr" to personIdent), headers)
    }
}
