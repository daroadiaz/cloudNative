# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
COPY Wallet_EcommerceCloudNative ./Wallet_EcommerceCloudNative

RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache bash curl

RUN addgroup -g 1000 spring && \
    adduser -u 1000 -G spring -s /bin/sh -D spring

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/Wallet_EcommerceCloudNative ./Wallet_EcommerceCloudNative

RUN mkdir -p /app/logs && chown -R spring:spring /app

USER spring

EXPOSE 8091

ENV TNS_ADMIN=/app/Wallet_EcommerceCloudNative

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/app.jar"]