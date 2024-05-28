package no.nav.familie.ef.proxy

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.github.tomakehurst.wiremock.WireMockServer
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ApplicationLocal::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrasjonstest")
@EnableMockOAuth2Server
abstract class IntegrationSpringRunnerTest {
    protected val listAppender = initLoggingEventListAppender()
    protected var loggingEvents: MutableList<ILoggingEvent> = listAppender.list
    protected val restTemplate = TestRestTemplate()
    protected val headers = HttpHeaders()

    @Autowired private lateinit var applicationContext: ApplicationContext

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private lateinit var mockOAuth2Server: MockOAuth2Server

    @LocalServerPort
    private var port: Int? = 0

    @AfterEach
    fun reset() {
        headers.clear()
        loggingEvents.clear()
        resetWiremockServers()
    }

    private fun resetWiremockServers() {
        applicationContext.getBeansOfType(WireMockServer::class.java).values.forEach(WireMockServer::resetRequests)
    }

    protected fun getPort(): String {
        return port.toString()
    }

    protected fun localhost(uri: String): String {
        return LOCALHOST + getPort() + uri
    }

    protected fun url(
        baseUrl: String,
        uri: String,
    ): String {
        return baseUrl + uri
    }

    protected val lokalTestToken: String
        get() {
            return clientToken()
        }

    protected fun clientToken(
        clientId: String = "1",
        accessAsApplication: Boolean = true,
    ): String {
        return TokenUtil.clientToken(mockOAuth2Server, clientId, accessAsApplication)
    }

    companion object {
        private const val LOCALHOST = "http://localhost:"

        protected fun initLoggingEventListAppender(): ListAppender<ILoggingEvent> {
            val listAppender = ListAppender<ILoggingEvent>()
            listAppender.start()
            return listAppender
        }
    }
}
