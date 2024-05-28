package no.nav.familie.ef.proxy.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.mockk.mockk
import no.nav.familie.ef.proxy.integration.SakClient
import no.nav.familie.ef.proxy.security.StsValidator
import no.nav.familie.kontrakter.felles.ef.EksternePerioderRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestOperations
import java.net.URI

internal class PeriodeControllerTest {
    @Test
    internal fun `skal hente perioder`() {
        val response = periodeController.hentPerioder(EksternePerioderRequest(""))
        assertThat(response.perioder).isEmpty()
    }

    companion object {
        private val restOperations: RestOperations = RestTemplateBuilder().build()
        lateinit var wiremockServerItem: WireMockServer
        lateinit var periodeController: PeriodeController

        @BeforeAll
        @JvmStatic
        fun initClass() {
            wiremockServerItem = WireMockServer(wireMockConfig().dynamicPort())
            wiremockServerItem.start()
            val stsValidator = mockk<StsValidator>(relaxed = true)
            val sakUri = URI.create("http://localhost:${wiremockServerItem.port()}/sak")

            val response = """{"status": "SUKSESS", "data": {"perioder":[]}, "melding": ""}"""

            wiremockServerItem.stubFor(post(urlEqualTo("/integrasjoner/api/infotrygd/vedtak-perioder")).willReturn(okJson(response)))
            wiremockServerItem.stubFor(post(urlEqualTo("/sak/api/ekstern/perioder")).willReturn(okJson(response)))

            val sakClient = SakClient(restOperations, sakUri)
            periodeController = PeriodeController(sakClient, stsValidator)
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServerItem.stop()
        }
    }

    @AfterEach
    fun tearDownEachTest() {
        wiremockServerItem.resetAll()
    }
}
