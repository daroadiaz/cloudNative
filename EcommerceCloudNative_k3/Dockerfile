# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivo POM primero para aprovechar la caché de Docker
COPY pom.xml .

# Descargar dependencias - esto se cachea si el POM no cambia
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

# Crear usuario no root para seguridad
RUN addgroup -g 1000 spring && \
    adduser -u 1000 -G spring -s /bin/sh -D spring

# Establecer directorio de trabajo
WORKDIR /app

# Copiar JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Copiar wallet de Oracle desde la etapa de construcción
COPY --from=builder /app/Wallet_EcommerceCloudNative ./Wallet_EcommerceCloudNative

# Crear directorio de logs con permisos correctos
RUN mkdir -p /app/logs && chown -R spring:spring /app

# Cambiar al usuario spring
USER spring

# Exponer puerto del microservicio
EXPOSE 8093

# Variables de entorno para Oracle
ENV TNS_ADMIN=/app/Wallet_EcommerceCloudNative

# Configuración JVM para optimización
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=100"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8093/api/promociones/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar -Dspring.profiles.active=docker /app/app.jar"]