package no.nav.familie.ef.proxy.integration

import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import no.nav.familie.kontrakter.felles.ef.PerioderOvergangsstønadRequest
import no.nav.familie.kontrakter.felles.ef.PerioderOvergangsstønadResponse
import no.nav.familie.kontrakter.felles.getDataOrThrow
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class FamilieIntegrasjonerClient(@Qualifier("azure") restOperations: RestOperations,
                                 @Value("\${FAMILIE_INTEGRASJONER_URL}") private val integrasjonUri: URI)
    : AbstractPingableRestClient(restOperations, "familie.integrasjoner") {

    private val vedtaksperioderUri =
            UriComponentsBuilder.fromUri(integrasjonUri).pathSegment("api/infotrygd/vedtak-perioder").build().toUri()

    override val pingUri: URI = UriComponentsBuilder.fromUri(integrasjonUri).pathSegment("api/ping").build().toUri()


    fun hentInfotrygdPerioder(request: PerioderOvergangsstønadRequest): PerioderOvergangsstønadResponse {
        return postForEntity<Ressurs<PerioderOvergangsstønadResponse>>(vedtaksperioderUri, request).getDataOrThrow()
    }

}
