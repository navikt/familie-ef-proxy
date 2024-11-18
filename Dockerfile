FROM gcr.io/distroless/java21-debian12:nonroot

ENV TZ="Europe/Oslo"
ENV APP_NAME=familie-ef-proxy

COPY init.sh /init-scripts/init.sh
COPY ./target/familie-ef-proxy.jar  /app.jar
COPY ./target /
ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=75"
CMD ["-jar", "/app.jar"]

