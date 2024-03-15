FROM gcr.io/distroless/java21-debian12:nonroot

ENV TZ="Europe/Oslo"
ENV APP_NAME=familie-ef-proxy

COPY init.sh /init-scripts/init.sh
COPY ./target/familie-ef-proxy-jar-with-dependencies.jar  /app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
CMD ["-jar", "/app.jar"]

