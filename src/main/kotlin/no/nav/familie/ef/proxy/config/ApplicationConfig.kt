package no.nav.familie.ef.proxy.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.familie.http.config.RestTemplateAzure
import no.nav.familie.http.config.RestTemplateSts
import no.nav.familie.http.sts.StsRestClient
import no.nav.familie.log.filter.LogFilter
import no.nav.familie.log.filter.RequestTimeFilter
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootConfiguration
@ConfigurationPropertiesScan
@ComponentScan("no.nav.familie.ef.proxy", "no.nav.familie.sikkerhet")
@EnableJwtTokenValidation(ignore = ["org.springframework", "springfox.documentation.swagger"])
@Import(RestTemplateAzure::class)
@EnableOAuth2Client(cacheEnabled = true)
@EnableScheduling
class ApplicationConfig {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun kotlinModule(): KotlinModule = KotlinModule()

    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> {
        logger.info("Registering LogFilter filter")
        val filterRegistration = FilterRegistrationBean<LogFilter>()
        filterRegistration.filter = LogFilter()
        filterRegistration.order = 1
        return filterRegistration
    }

    @Bean
    fun requestTimeFilter(): FilterRegistrationBean<RequestTimeFilter> {
        logger.info("Registering RequestTimeFilter filter")
        val filterRegistration = FilterRegistrationBean<RequestTimeFilter>()
        filterRegistration.filter = RequestTimeFilter()
        filterRegistration.order = 2
        return filterRegistration
    }

    companion object {

        private const val API_KEY_HEADER = "x-nav-apiKey"
    }
}
