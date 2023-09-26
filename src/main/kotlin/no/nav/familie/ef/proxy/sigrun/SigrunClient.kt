package no.nav.familie.ef.proxy.sigrun

import no.nav.familie.http.client.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class SigrunClient(
    @Value("\${SIGRUN_URL}") private val uri: URI,
    @Qualifier("azure") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "sigrun") {

    fun hentPensjonsgivendeInntekt(personIdent: String, inntektsår: Int): Map<String, Any> {
        val uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).pathSegment("v1/pensjonsgivendeinntektforfolketrygden")

        val headers = HttpHeaders()
        headers.set("Nav-Personident", personIdent)
        headers.set("norskident", personIdent) // Kan fjernes når pensjonsgivende inntekt er tilgjengelig i Dolly
        headers.set("inntektsaar", inntektsår.toString())

        return try {
            getForEntity(uriComponentsBuilder.build().toUri(), headers)
        } catch (e: HttpClientErrorException.NotFound) {
            secureLogger.warn(e.message)
            emptyMap()
        }
    }

    fun hentBeregnetSkatt(personIdent: String, inntektsår: Int): List<Map<String, Any>> {
        val uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).pathSegment("beregnetskatt")
            .queryParam("inntektsaar", inntektsår)

        val headers = HttpHeaders()
        headers.set("x-filter", "BeregnetSkattPensjonsgivendeInntekt")
        headers.set("x-naturligident", personIdent)
        headers.set("x-inntektsaar", inntektsår.toString())

        return try {
            getForEntity(uriComponentsBuilder.build().toUri(), headers)
        } catch (e: HttpClientErrorException.NotFound) {
            secureLogger.warn(e.message)
            emptyList()
        }
    }

    fun hentSummertSkattegrunnlag(personIdent: String, inntektsår: Int): List<Map<String, Any>> {
        val uriComponentsBuilder = UriComponentsBuilder.fromUri(uri).pathSegment("v1/summertskattegrunnlag")
            .queryParam("inntektsaar", inntektsår)
            .queryParam("inntektsfilter", "SummertSkattegrunnlagEnsligForsorger")

        val headers = HttpHeaders()
        headers.set("x-naturligident", personIdent)

        return try {
            getForEntity(uriComponentsBuilder.build().toUri(), headers)
        } catch (e: HttpClientErrorException.NotFound) {
            secureLogger.warn(e.message)
            emptyList()
        }
    }
}
