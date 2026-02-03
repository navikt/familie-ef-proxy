package no.nav.familie.ef.proxy.sigrun

import no.nav.familie.http.client.AbstractRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
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
    fun hentPensjonsgivendeInntekt(
        personIdent: String,
        inntektsår: Int,
    ): Map<String, Any> {
        val url =
            UriComponentsBuilder
                .fromUri(uri)
                .pathSegment("v1", "pensjonsgivendeinntektforfolketrygden")
                .build()
                .toUri()

        val request =
            PensjonsgivendeInntektRequest(
                personident = personIdent,
                inntektsaar = inntektsår.toString(),
            )

        return try {
            postForEntity(url, request)
        } catch (e: HttpClientErrorException.NotFound) {
            secureLogger.warn(e.message)
            emptyMap()
        }
    }
}

data class PensjonsgivendeInntektRequest(
    val personident: String,
    val inntektsaar: String,
    val rettighetspakke: String = "navEnsligForsoerger",
)
