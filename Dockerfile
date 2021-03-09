FROM navikt/java:11

COPY init.sh /init-scripts/init.sh

ENV APP_NAME=familie-ef-proxy
COPY ./target/familie-ef-proxy.jar "app.jar"