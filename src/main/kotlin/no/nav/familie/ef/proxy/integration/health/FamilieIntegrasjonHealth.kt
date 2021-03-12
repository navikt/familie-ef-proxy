package no.nav.familie.ef.proxy.integration.health

import no.nav.familie.ef.proxy.integration.FamilieIntegrasjonerClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!local")
class FamilieIntegrasjonHealth(client: FamilieIntegrasjonerClient)
    : AbstractHealthIndicator(client, "familie.integrasjoner")
