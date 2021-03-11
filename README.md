# familie-ef-proxy

Applikasjon som kan brukes mellom gcp<->on-prem for kommunikasjon med tjenester. Eks for arena som kaller på ef-sak med sts token,
så mottar denne sts, og kaller videre på ef-sak med azure-token

### Deployments

Appen deployes til teamfamilie, både i dev og prod. I tillegg deployes en versjon av appen som "familie-ef-proxy-test" som
deployes i dev, men som bruker STS i test, slik att arena kan kalle på oss i "test" som kaller videre på ef-sak i dev. Dette for å
unngå å ha ef-sak deployet i dev, test og prod