#!/bin/bash

# Script de despliegue para la aplicación E-commerce

echo "🚀 Iniciando proceso de despliegue..."

# Verificar que existe el wallet de Oracle
if [ ! -d "Wallet_EcommerceCloudNative" ]; then
    echo "❌ Error: No se encontró el directorio Wallet_EcommerceCloudNative"
    echo "Por favor, asegúrate de copiar el wallet en el directorio raíz del proyecto"
    exit 1
fi

# Limpiar contenedores anteriores
echo "🧹 Limpiando contenedores anteriores..."
docker-compose down 2>/dev/null || true

# Construir la imagen
echo "🔨 Construyendo imagen Docker..."
docker build -t ecommerce-api:latest .

# Verificar si la construcción fue exitosa
if [ $? -eq 0 ]; then
    echo "✅ Imagen construida exitosamente"
else
    echo "❌ Error al construir la imagen"
    exit 1
fi

# Crear directorio de logs si no existe
mkdir -p logs

# Iniciar el contenedor con docker-compose
echo "🎯 Iniciando contenedor..."
docker-compose up -d

# Esperar a que el servicio esté listo
echo "⏳ Esperando a que el servicio inicie..."
sleep 10

# Verificar el estado del contenedor
if docker ps | grep -q ecommerce-springboot; then
    echo "✅ Contenedor iniciado correctamente"
    echo "📋 Logs del contenedor:"
    docker logs --tail 20 ecommerce-springboot
    echo ""
    echo "🌐 La aplicación está disponible en: http://localhost:8080"
    echo "🔐 Usuario: Admin"
    echo "🔑 Contraseña: CloudNative_123"
else
    echo "❌ Error: El contenedor no está ejecutándose"
    docker logs ecommerce-springboot
    exit 1
fi