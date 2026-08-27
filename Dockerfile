# syntax=docker/dockerfile:1

FROM maven:4.0.0-rc-5-eclipse-temurin-25-noble AS build

ARG APP_VERSION=0.7.1
ARG APP_NAME="DontForget"

WORKDIR /app
# copy the pom.xml into "app" directory
COPY pom.xml .
# Then check dependencies
RUN mvn clean compile
# Then copy rest
COPY src ./src
# Then package it
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jre

# installing X11 libraries
RUN apt-get update && \
    apt-get install -y \
    libxext6 libxrender1 libxtst6 libxi6 \
    libx11-6 libxft2 fonts-dejavu \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/target/DontForget*.jar app.jar

EXPOSE 8090

CMD ["java", "-jar", "app.jar"]