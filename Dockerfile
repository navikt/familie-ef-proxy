FROM navikt/java:11

ENV APP_NAME=familie-ef-proxy
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
COPY init.sh /init-scripts/init.sh
COPY ./target/familie-ef-proxy.jar "app.jar"