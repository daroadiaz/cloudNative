# Etapa de construcción
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Copiar el wallet de Oracle que está en la raíz del proyecto
COPY Wallet_EcommerceCloudNative ./Wallet_EcommerceCloudNative

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiar el JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Copiar el wallet de Oracle
COPY --from=build /app/Wallet_EcommerceCloudNative /app/Wallet_EcommerceCloudNative

# Variables de entorno para Oracle
ENV TNS_ADMIN=/app/Wallet_EcommerceCloudNative

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]