FROM navikt/java:11

ENV APP_NAME=familie-ef-proxy

COPY init.sh /init-scripts/init.sh
COPY ./target/familie-ef-proxy.jar "app.jar"