package no.nav.familie.ef.proxy.sigrun

import io.mockk.every
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("integrasjonstest")
class SigrunTestConfig {

    @Bean
    @Primary
    fun sigrunClient(): SigrunClient {
        val client = mockk<SigrunClient>()
        every { client.hentPensjonsgivendeInntekt(any(), any()) } returns emptyMap()
        every { client.hentBeregnetSkatt(any(), any()) } returns emptyList()
        every { client.hentSummertSkattegrunnlag(any(), any()) } returns emptyList()
        return client
    }
}
