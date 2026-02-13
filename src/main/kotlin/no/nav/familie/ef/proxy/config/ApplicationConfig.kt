package no.nav.familie.ef.proxy.config

import no.nav.familie.kontrakter.felles.jsonMapper
import no.nav.familie.log.NavSystemtype
import no.nav.familie.log.filter.LogFilter
import no.nav.familie.log.filter.RequestTimeFilter
import no.nav.familie.restklient.client.RetryOAuth2HttpClient
import no.nav.familie.restklient.config.NaisProxyCustomizer
import no.nav.familie.restklient.config.RestTemplateAzure
import no.nav.familie.restklient.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.restklient.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.restklient.sts.StsRestClient
import no.nav.security.token.support.client.core.http.OAuth2HttpClient
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.time.temporal.ChronoUnit

@SpringBootConfiguration
@ConfigurationPropertiesScan
@ComponentScan("no.nav.familie.ef.proxy", "no.nav.familie.sikkerhet")
@EnableJwtTokenValidation(ignore = ["org.springframework", "org.springdoc"])
@Import(RestTemplateAzure::class, RestTemplateSts::class, StsRestClient::class)
@EnableOAuth2Client(cacheEnabled = true)
@EnableScheduling
class ApplicationConfig {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    @Primary
    fun jsonMapper() = jsonMapper

    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> {
        logger.info("Registering LogFilter filter")
        val filterRegistration = FilterRegistrationBean(LogFilter(systemtype = NavSystemtype.NAV_INTEGRASJON))
        filterRegistration.order = 1
        return filterRegistration
    }

    @Bean
    fun requestTimeFilter(): FilterRegistrationBean<RequestTimeFilter> {
        logger.info("Registering RequestTimeFilter filter")
        val filterRegistration = FilterRegistrationBean(RequestTimeFilter())
        filterRegistration.order = 2
        return filterRegistration
    }

    @Bean
    fun restOperations(
        restTemplateBuilder: RestTemplateBuilder,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        restTemplateBuilder
            .additionalMessageConverters(listOf(JacksonJsonHttpMessageConverter(jsonMapper)) + RestTemplate().messageConverters)
            .additionalInterceptors(
                consumerIdClientInterceptor,
                MdcValuesPropagatingClientInterceptor(),
            ).build()

    @Primary
    @Bean
    fun oAuth2HttpClient(): OAuth2HttpClient =
        RetryOAuth2HttpClient(
            RestClient.create(
                RestTemplateBuilder()
                    .additionalCustomizers(NaisProxyCustomizer(2_000, 2_000, 4_000))
                    .additionalMessageConverters(listOf(JacksonJsonHttpMessageConverter(jsonMapper)) + RestTemplate().messageConverters)
                    .connectTimeout(Duration.of(2, ChronoUnit.SECONDS))
                    .readTimeout(Duration.of(4, ChronoUnit.SECONDS))
                    .build(),
            ),
        )
}
