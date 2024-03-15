FROM gcr.io/distroless/java21-debian12:nonroot

ENV TZ="Europe/Oslo"
ENV APP_NAME=familie-ef-proxy

COPY init.sh /init-scripts/init.sh
COPY ./target/familie-ef-proxy.jar  /app.jar
COPY ./target /

CMD ["-XX:MaxRAMPercentage=75", "-jar", "/app.jar"]

