FROM gcr.io/distroless/java21-debian12
LABEL maintainer="PO Familie - Enslig forsørger"
ENV LC_ALL='nb_NO.UTF-8'
ENV TZ='Europe/Oslo'
ENV APP_NAME=familie-ef-proxy
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

COPY init.sh /init-scripts/init.sh
COPY ./target/familie-ef-proxy.jar "app.jar"
CMD ["-jar", "/app/app.jar"]