package no.nav.familie.ef.proxy.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("integrasjonstest")
class StsValidatorTestConfig {
    @Bean
    @Primary
    fun stsValidator(): StsValidator {
        return object : StsValidator {
            override fun validateSts(subject: String) {
            }
        }
    }
}
