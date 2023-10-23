package no.nav.familie.ef.proxy.ainntekt

import no.nav.familie.http.client.AbstractRestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class ArbeidOgInntektClient(
    @Value("\${ARBEID_INNTEKT_URL}") private val uri: URI,
    restOperations: RestOperations,
) : AbstractRestClient(restOperations, "ereg") {

    private val redirectUri = UriComponentsBuilder.fromUri(uri)
        .pathSegment("api/v2/redirect/sok/a-inntekt").build().toUri()

    private val redirectUriArbeidsforhold = UriComponentsBuilder.fromUri(uri)
        .pathSegment("api/v2/redirect/sok/arbeidstaker").build().toUri()

    fun hentUrlTilArbeidOgInntekt(personIdent: String): String {
        return getForEntity(
            redirectUri,
            HttpHeaders().apply {
                accept = listOf(MediaType.TEXT_PLAIN)
                set("Nav-Personident", personIdent)
            },
        )
    }

    fun hentUrlTilArbeidsforhold(personIdent: String): String {
        return getForEntity(
            redirectUriArbeidsforhold,
            HttpHeaders().apply {
                accept = listOf(MediaType.TEXT_PLAIN)
                set("Nav-Personident", personIdent)
            },
        )
    }
}
