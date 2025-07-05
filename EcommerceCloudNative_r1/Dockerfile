# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:go-offline

# Copiar código fuente
COPY src ./src

# Copiar wallet de Oracle (si existe)
COPY Wallet_EcommerceCloudNative ./Wallet_EcommerceCloudNative

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre-alpine

# Instalar dependencias necesarias
RUN apk add --no-cache bash curl

# Crear usuario no root
RUN addgroup -g 1000 spring && \
    adduser -u 1000 -G spring -s /bin/sh -D spring

# Establecer directorio de trabajo
WORKDIR /app

# Copiar JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Copiar wallet de Oracle
COPY --from=builder /app/Wallet_EcommerceCloudNative ./Wallet_EcommerceCloudNative

# Crear directorio de logs
RUN mkdir -p /app/logs && chown -R spring:spring /app

# Cambiar al usuario spring
USER spring

# Exponer puerto
EXPOSE 8081

# Variables de entorno para Oracle
ENV TNS_ADMIN=/app/Wallet_EcommerceCloudNative

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/app.jar"]