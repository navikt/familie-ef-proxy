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
    @Value("\${INNTEKT_URL}")
    private val uri: URI,
    @Value("\${INNTEKTV2_URL}")
    private val inntektV2Uri: URI,
    private val stsClient: StsClient,
    @Qualifier("noToken") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "inntekt") {
    private val inntektUri =
        UriComponentsBuilder
            .fromUri(uri)
            .pathSegment("v1/hentinntektliste")
            .build()
            .toUri()

    private val inntektUriV2 =
        UriComponentsBuilder
            .fromUri(inntektV2Uri)
            .pathSegment("inntekt")
            .build()
            .toUri()

    fun hentInntekt(
        personIdent: String,
        fom: YearMonth,
        tom: YearMonth,
    ): Map<String, Any> =
        postForEntity(
            inntektUri,
            lagRequest(personIdent, fom, tom),
            headers(personIdent, stsClient.hentStsToken().token),
        )

    fun hentInntektV2(
        personident: String,
        maanedFom: YearMonth,
        maanedTom: YearMonth,
    ): Map<String, Any> =
        postForEntity(
            uri = inntektUriV2,
            payload =
                lagRequestV2(
                    personident = personident,
                    maanedFom = maanedFom,
                    maanedTom = maanedTom,
                ),
            httpHeaders = headersv2(personident, stsClient.hentStsToken().token),
        )

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
        return getForEntity(inntektshistorikkUri, headers(personIdent, stsClient.hentStsToken().token))
    }

    private fun lagRequest(
        personIdent: String,
        fom: YearMonth,
        tom: YearMonth,
    ) = mapOf(
        "ainntektsfilter" to "StoenadEnsligMorEllerFarA-inntekt",
        "formaal" to "StoenadEnsligMorEllerFar",
        "ident" to
            mapOf(
                "identifikator" to personIdent,
                "aktoerType" to "NATURLIG_IDENT",
            ),
        "maanedFom" to fom,
        "maanedTom" to tom,
    )

    private fun lagRequestV2(
        personident: String,
        maanedFom: YearMonth,
        maanedTom: YearMonth,
    ) = mapOf(
        "personident" to personident,
        "filter" to "StoenadEnsligMorEllerFarA-inntekt",
        "formaal" to "StoenadEnsligMorEllerFar",
        "maanedFom" to maanedFom,
        "maanedTom" to maanedTom,
    )

    private fun headers(
        personIdent: String,
        token: String,
    ): HttpHeaders =
        HttpHeaders().apply {
            setBearerAuth(token)
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
            add(NavHttpHeaders.NAV_PERSONIDENT.asString(), personIdent)
        }

    private fun headersv2(
        personident: String,
        token: String,
    ): HttpHeaders =
        HttpHeaders().apply {
            setBearerAuth(token)
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
            add(NavHttpHeaders.NAV_PERSONIDENT.asString(), personident)
        }
}
