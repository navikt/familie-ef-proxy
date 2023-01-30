package no.nav.familie.ef.proxy.ainntekt

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import no.nav.familie.kontrakter.felles.PersonIdent
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestOperations
import java.net.URI

internal class ArbeidOgInntektControllerTest {

    @Test
    internal fun `skal hente url fra arbeid og inntekt`() {
        val response = controller.hentUrlTilArbeidOgInntekt(PersonIdent("1"))
        Assertions.assertThat(response).isEqualTo("http://testurl")
    }

    companion object {

        private val restOperations: RestOperations = RestTemplateBuilder().build()
        lateinit var wiremockServerItem: WireMockServer
        lateinit var controller: ArbeidOgInntektController

        @BeforeAll
        @JvmStatic
        fun initClass() {
            wiremockServerItem = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())
            wiremockServerItem.start()
            val uri = URI.create("http://localhost:${wiremockServerItem.port()}")

            wiremockServerItem.stubFor(
                WireMock.get(WireMock.urlEqualTo("/api/v2/redirect/sok/a-inntekt"))
                    .willReturn(WireMock.okForContentType("text/html", "http://testurl")),
            )

            controller = ArbeidOgInntektController(ArbeidOgInntektClient(uri, restOperations))
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
