FROM navikt/java:11

ENV APP_NAME=familie-ef-proxy
COPY ./target/familie-ef-proxy.jar "app.jar"