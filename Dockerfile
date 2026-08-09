# syntax=docker/dockerfile:1

FROM maven:4.0.0-rc-5-eclipse-temurin-25-noble AS build

ARG APP_VERSION=0.7.1
ARG APP_NAME="DontForget"

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -Dproject.build.finalName=${APP_NAME}-${APP_VERSION} -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jre

ARG APP_VERSION=0.7.1
ARG APP_NAME="DontForget"

WORKDIR /app

COPY --from=build /app/target/${APP_NAME}-${APP_VERSION}.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
