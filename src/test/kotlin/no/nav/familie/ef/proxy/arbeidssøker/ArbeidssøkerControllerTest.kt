package no.nav.familie.ef.proxy.arbeidssøker

import io.mockk.verify
import no.nav.familie.ef.proxy.IntegrationSpringRunnerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.time.LocalDate

internal class ArbeidssøkerControllerTest : IntegrationSpringRunnerTest() {

    @Autowired lateinit var arbeidssøkerClient: ArbeidssøkerClient

    @BeforeEach
    fun setUp() {
        headers.setBearerAuth(lokalTestToken)
    }

    @Test
    internal fun `skal kalle på perioder uten tilOgMedDato`() {
        val url = localhost("/api/arbeidssoker/perioder?fraOgMed=2021-01-01")
        val response = restTemplate.exchange<Map<String, String>>(url,
                                                                  HttpMethod.POST,
                                                                  HttpEntity(mapOf("ident" to "123"), headers))
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!).isEqualTo(emptyMap<String, Any>())

        verify(exactly = 1) { arbeidssøkerClient.hentPerioder("123", LocalDate.of(2021, 1, 1), null) }
    }

    @Test
    internal fun `skal kalle på perioder med tilOgMedDato`() {
        val url = localhost("/api/arbeidssoker/perioder?fraOgMed=2021-01-01&tilOgMed=2021-02-01")
        val response = restTemplate.exchange<Map<String, String>>(url,
                                                                  HttpMethod.POST,
                                                                  HttpEntity(mapOf("ident" to "123"), headers))
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!).isEqualTo(emptyMap<String, Any>())

        verify(exactly = 1) { arbeidssøkerClient.hentPerioder("123", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)) }
    }
}