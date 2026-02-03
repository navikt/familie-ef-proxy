package no.nav.familie.ef.proxy.sigrun

import io.mockk.verify
import no.nav.familie.ef.proxy.IntegrationSpringRunnerTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.time.YearMonth

internal class SigrunControllerTest : IntegrationSpringRunnerTest() {
    @Autowired lateinit var sigrunClient: SigrunClient

    @BeforeEach
    fun setUp() {
        headers.setBearerAuth(lokalTestToken)
    }

    @Test
    internal fun `kall pensjonsgivendeinntekt med inntektsaar`() {
        val url = localhost("/api/sigrun/pensjonsgivendeinntekt?inntektsaar=2022")
        val response =
            restTemplate.exchange<Map<String, String>>(
                url,
                HttpMethod.POST,
                HttpEntity(mapOf("ident" to "123"), headers),
            )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body!!).isEqualTo(emptyMap<String, Any>())

        verify(exactly = 1) { sigrunClient.hentPensjonsgivendeInntekt("123", 2022) }
    }
}
