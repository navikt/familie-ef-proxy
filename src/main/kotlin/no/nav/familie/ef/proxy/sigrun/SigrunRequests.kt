package no.nav.familie.ef.proxy.sigrun

data class PensjonsgivendeInntektRequest(
    val personidentifikator: String,
    val inntektsaar: Int,
    val rettighetspakke: String = "navEnsligForsoerger",
)

data class SummertSkattegrunnlagRequest(
    val personidentifikator: String,
    val inntektsaar: Int,
    val inntektsfilter: String = "SummertSkattegrunnlagEnsligForsorger",
)
