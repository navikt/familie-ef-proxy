package no.nav.familie.ef.proxy.config

import no.nav.familie.http.config.RestTemplateBuilderBean
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.kontrakter.felles.objectMapper
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(RestTemplateBuilderBean::class, ConsumerIdClientInterceptor::class)
class RestTemplateSts {
    @Bean("noToken")
    fun restTemplateNoToken(
        restTemplateBuilder: RestTemplateBuilder,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        restTemplateBuilder
            .additionalMessageConverters(
                listOf(MappingJackson2HttpMessageConverter(objectMapper)) + RestTemplate().messageConverters,
            ).additionalInterceptors(
                consumerIdClientInterceptor,
                MdcValuesPropagatingClientInterceptor(),
            ).build()
}
