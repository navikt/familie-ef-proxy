package no.nav.familie.ef.proxy.integration

import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.log.NavHttpHeaders
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.YearMonth

@Component
class InntektClient(
        @Value("\${INNTEKT_URL}") private val uri: URI,
        @Qualifier("sts") restOperations: RestOperations
) : AbstractRestClient(restOperations, "inntekt") {

    fun hentInntekt(personIdent: String,
                    fom: YearMonth,
                    tom: YearMonth): Map<String, Any> {
        val uriBuilder = UriComponentsBuilder.fromUri(uri).pathSegment("v1/hentinntektliste")
                .queryParam("maaned-fom", fom.toString())
                .queryParam("maaned-tom", tom.toString())
                .queryParam("filter", "StoenadEnsligMorEllerFarA-inntekt")

        return getForEntity(uriBuilder.build().toUri(), headers(personIdent))
    }

    private fun headers(personIdent: String): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
            add(NavHttpHeaders.NAV_PERSONIDENT.asString(), personIdent)
        }
    }

}
