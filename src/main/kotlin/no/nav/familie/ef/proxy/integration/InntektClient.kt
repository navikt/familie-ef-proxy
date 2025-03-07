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
    @Value("\${INNTEKTV2_URL}")
    private val inntektV2Uri: URI,
    private val stsClient: StsClient,
    @Qualifier("noToken") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "inntekt") {
    private val inntektUri =
        UriComponentsBuilder
            .fromUri(inntektV2Uri)
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
                maanedFom = maanedFom,
                maanedTom = maanedTom,
            )

        val payload = objectMapper.writeValueAsString(request)

        val entity =
            postForEntity<Map<String, Any>>(
                uri = inntektUri,
                payload = payload,
                httpHeaders =
                    headers(
                        token = stsClient.hentStsToken().token,
                    ),
            )

        return entity
    }

    fun hentInntektshistorikk(
        personIdent: String,
        fom: YearMonth,
        tom: YearMonth,
    ): Map<String, Any> {
        val inntektshistorikkUri =
            UriComponentsBuilder
                .fromUri(uri)
                .pathSegment("v1/inntektshistorikk")
                .queryParam("maaned-fom", fom)
                .queryParam("maaned-tom", tom)
                .queryParam("filter", "StoenadEnsligMorEllerFarA-inntekt")
                .build()
                .toUri()
        return getForEntity(inntektshistorikkUri, headers(stsClient.hentStsToken().token))
    }

    private fun genererInntektRequest(
        personIdent: String,
        maanedFom: YearMonth,
        maanedTom: YearMonth,
    ) = mapOf(
        "personident" to personIdent,
        "filter" to "StoenadEnsligMorEllerFarA-inntekt",
        "formaal" to "StoenadEnsligMorEllerFar",
        "maanedFom" to maanedFom,
        "maanedTom" to maanedTom,
    )

    private fun headers(
        token: String,
    ): HttpHeaders =
        HttpHeaders().apply {
            setBearerAuth(token)
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }
}
