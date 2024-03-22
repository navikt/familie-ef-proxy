package no.nav.familie.ef.proxy.integration

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@Component
class StsClient(
    @Value("\${STS_URL}")
    private val stsUri: URI,
    @Value("\${CREDENTIAL_USERNAME}")
    private val usernameEfSak: String,
    @Value("\${CREDENTIAL_PASSWORD}")
    private val passwordEfSak: String,
    @Value("\${SRVUSERNAME_EF_PERSONHENDELSE}")
    private val usernamePersonhendelse: String,
    @Value("\${SRVPASSWORD_EF_PERSONHENDELSE}")
    private val passwordPersonhendelse: String,
    @Value("\${EF_PERSONHENDELSE_CLIENT_ID}")
    private val personhendelseClientId: String,
    @Value("\${EF_SAK_CLIENT_ID}")
    private val efSakClientId: String,
) {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun hentStsToken(): Token {
        val stsUri = UriComponentsBuilder.fromUri(stsUri)
            .queryParam("grant_type", "client_credentials")
            .queryParam("scope", "openid")
            .toUriString()
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        val clientId = SpringTokenValidationContextHolder().getTokenValidationContext().getClaims("azuread").get("azp_name") as String
        headers.setBasicAuth(credentialForClientId(clientId))
        val entity = HttpEntity<String>(headers)

        val response = RestTemplate().exchange(stsUri, HttpMethod.POST, entity, String::class.java)
        if (response.body != null) {
            return objectMapper.readValue(response.body, Token::class.java)
        } else {
            throw Exception("Feil i kall mot STS: Returnerte ingen token")
        }
    }

    fun credentialForClientId(clientId: String): String {
        return mapOf(personhendelseClientId to credentialsPersonhendelse(), efSakClientId to credentialsEfSak()).getOrElse(clientId) {
            secureLogger.warn("Fant ikke clientId: $clientId i map. Bruker ef-sak token.")
            credentialsEfSak()
        }
    }

    private fun credentialsPersonhendelse() = Base64.getEncoder().encodeToString("$usernamePersonhendelse:$passwordPersonhendelse".toByteArray(Charsets.UTF_8))
    private fun credentialsEfSak() = Base64.getEncoder().encodeToString("$usernameEfSak:$passwordEfSak".toByteArray(Charsets.UTF_8))

    private fun Token?.shouldBeRenewed(): Boolean = this?.hasExpired() ?: true

    data class Token(
        @JsonProperty(value = "access_token", required = true)
        val token: String,
        @JsonProperty(value = "token_type", required = true)
        val type: String,
        @JsonProperty(value = "expires_in", required = true)
        val expiresIn: Int,
    ) {

        private val expirationTime: LocalDateTime = LocalDateTime.now().plusSeconds(expiresIn - 20L)

        fun hasExpired(): Boolean = expirationTime.isBefore(LocalDateTime.now())
    }
}
