package no.nav.familie.ef.proxy.integration

import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!local")
class SakHealth(client: SakClient) : AbstractHealthIndicator(client, "familie.sak")
