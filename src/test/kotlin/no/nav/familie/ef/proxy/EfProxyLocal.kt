package no.nav.familie.ef.proxy

import no.nav.familie.ef.proxy.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class ApplicationLocal

fun main(args: Array<String>) {
    SpringApplicationBuilder(ApplicationConfig::class.java)
        .profiles("local")
        .run(*args)
}
