package no.nav.familie.ef.proxy.sigrun

data class PensjonsgivendeInntektRequest(
    val personident: String,
    val inntektsaar: String,
    val rettighetspakke: String = "navEnsligForsoerger",
)

data class SummertSkattegrunnlagRequest(
    val personident: String,
    val inntektsaar: String,
    val inntektsfilter: String = "SummertSkattegrunnlagEnsligForsorger",
)
