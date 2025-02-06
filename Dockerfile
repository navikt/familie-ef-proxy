FROM gcr.io/distroless/java21-debian12:nonroot
LABEL maintainer="PO Familie - Enslig forsørger"

ENV LANG='nb_NO.UTF-8'
ENV LANGUAGE='nb_NO:nb'
ENV LC_ALL='nb_NO.UTF-8'
ENV TZ='Europe/Oslo'
ENV APP_NAME=familie-ef-proxy
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

COPY ./target/familie-ef-proxy.jar /app/app.jar
CMD ["-jar", "/app/app.jar"]