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

    private val inntektUri = UriComponentsBuilder.fromUri(uri).pathSegment("v1/hentinntektliste").build().toUri()

    fun hentInntekt(personIdent: String,
                    fom: YearMonth,
                    tom: YearMonth): Map<String, Any> {

        return postForEntity(inntektUri, lagRequest(personIdent, fom, tom), headers(personIdent))
    }

    private fun lagRequest(personIdent: String,
                           fom: YearMonth,
                           tom: YearMonth) =
            mapOf("ainntektsfilter" to "StoenadEnsligMorEllerFarA-inntekt",
                  "formaal" to "StoenadEnsligMorEllerFar",
                  "ident" to mapOf("identifikator" to personIdent,
                                   "aktoerType" to "NATURLIG_IDENT"),
                  "maanedFom" to fom,
                  "maanedTom" to tom)

    private fun headers(personIdent: String): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
            add(NavHttpHeaders.NAV_PERSONIDENT.asString(), personIdent)
        }
    }

}
