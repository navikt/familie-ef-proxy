package no.nav.familie.ef.proxy.sigrun

import io.mockk.verify
import no.nav.familie.ef.proxy.IntegrationSpringRunnerTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.exchange
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
    internal fun `kall beregnetskatt med inntektsaar`() {
        val url = localhost("/api/sigrun/beregnetskatt?inntektsaar=2020")
        val response = restTemplate.exchange<Map<String, String>>(
            url,
            HttpMethod.POST,
            HttpEntity(mapOf("ident" to "123"), headers),
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body!!).isEqualTo(emptyMap<String, Any>())

        verify(exactly = 1) { sigrunClient.hentBeregnetSkatt("123", 2020) }
    }

    @Test
    internal fun `kall beregnetskatt uten inntektsaar`() {
        val url = localhost("/api/sigrun/beregnetskatt")
        val response = restTemplate.exchange<Map<String, String>>(
            url,
            HttpMethod.POST,
            HttpEntity(mapOf("ident" to "1234"), headers),
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body!!).isEqualTo(emptyMap<String, Any>())

        verify(exactly = 1) { sigrunClient.hentBeregnetSkatt("1234", YearMonth.now().year - 1) }
    }

    @Test
    internal fun `kall summertskattegrunnlag`() {
        val url = localhost("/api/sigrun/summertskattegrunnlag")
        val response = restTemplate.exchange<Map<String, String>>(
            url,
            HttpMethod.POST,
            HttpEntity(mapOf("ident" to "12345"), headers),
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body!!).isEqualTo(emptyMap<String, Any>())

        verify(exactly = 1) { sigrunClient.hentSummertSkattegrunnlag("12345", YearMonth.now().year - 1) }
    }
}
