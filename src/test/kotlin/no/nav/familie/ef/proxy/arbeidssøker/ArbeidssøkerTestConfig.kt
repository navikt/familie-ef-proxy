package no.nav.familie.ef.proxy.arbeidssøker

import io.mockk.every
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("integrasjonstest")
class ArbeidssøkerTestConfig {

    @Bean
    @Primary
    fun arbeidssøkerClient(): ArbeidssøkerClient {
        val client = mockk<ArbeidssøkerClient>()
        every { client.hentPerioder(any(), any(), any()) } returns emptyMap()
        return client
    }
}