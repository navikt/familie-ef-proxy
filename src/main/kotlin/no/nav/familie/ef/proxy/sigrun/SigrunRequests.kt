package no.nav.familie.ef.proxy.sigrun

import com.fasterxml.jackson.annotation.JsonProperty

data class PensjonsgivendeInntektRequest(
    @JsonProperty("personidentifikator")
    val personIdent: String,
    @JsonProperty("inntektsaar")
    val inntektsår: Int,
    val rettighetspakke: String = "navEnsligForsoerger",
)

data class SummertSkattegrunnlagRequest(
    @JsonProperty("personidentifikator")
    val personIdent: String,
    @JsonProperty("inntektsaar")
    val inntektsår: Int,
    val inntektsfilter: String = "SummertSkattegrunnlagEnsligForsorger",
)
