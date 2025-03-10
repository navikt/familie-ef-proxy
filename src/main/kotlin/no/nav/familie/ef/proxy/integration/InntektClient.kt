package no.nav.familie.ef.proxy.integration

import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.kontrakter.felles.objectMapper
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
    @Value("\${INNTEKT_URL}")
    private val uri: URI,
    private val stsClient: StsClient,
    @Qualifier("noToken") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "inntekt") {
    private val inntektV2Uri =
        UriComponentsBuilder
            .fromUri(uri)
            .pathSegment("inntekt")
            .build()
            .toUri()

    fun hentInntekt(
        personIdent: String,
        maanedFom: YearMonth,
        maanedTom: YearMonth,
    ): Map<String, Any> {
        val request =
            genererInntektRequest(
                personIdent = personIdent,
                månedFom = maanedFom,
                månedTom = maanedTom,
            )

        val payload = objectMapper.writeValueAsString(request)

        val entity =
            postForEntity<Map<String, Any>>(
                uri = inntektV2Uri,
                payload = payload,
                httpHeaders =
                    headers(
                        token = stsClient.hentStsToken().token,
                    ),
            )

        return entity
    }

    private fun genererInntektRequest(
        personIdent: String,
        månedFom: YearMonth,
        månedTom: YearMonth,
    ) = mapOf(
        "personident" to personIdent,
        "filter" to "StoenadEnsligMorEllerFarA-inntekt",
        "formaal" to "StoenadEnsligMorEllerFar",
        "maanedFom" to månedFom,
        "maanedTom" to månedTom,
    )

    private fun headers(token: String): HttpHeaders =
        HttpHeaders().apply {
            setBearerAuth(token)
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }
}
