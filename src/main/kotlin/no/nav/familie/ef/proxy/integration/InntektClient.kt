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
    private val stsClient: StsClient,
    @Qualifier("noToken") restOperations: RestOperations,
) : AbstractRestClient(restOperations, "inntekt") {
    private val inntektUri =
        UriComponentsBuilder
            .fromUri(uri)
            .pathSegment("v1/hentinntektliste")
            .build()
            .toUri()

    fun hentInntekt(
        personIdent: String,
        fom: YearMonth,
        tom: YearMonth,
    ): Map<String, Any> = postForEntity(inntektUri, lagRequest(personIdent, fom, tom), headers(personIdent, stsClient.hentStsToken().token))

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
}
