#!/bin/bash

echo "🚀 Configurando sistema de microservicios con Kafka..."

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Función para verificar si un comando existe
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Verificar Docker
if ! command_exists docker; then
    echo -e "${RED}❌ Docker no está instalado${NC}"
    exit 1
fi

# Verificar Docker Compose
if ! command_exists docker-compose; then
    echo -e "${RED}❌ Docker Compose no está instalado${NC}"
    exit 1
fi

echo "✅ Docker y Docker Compose detectados"

# Crear directorios necesarios
echo "📁 Creando directorios..."
mkdir -p logs/ventas logs/inventario logs/promociones

# Copiar wallet a cada microservicio
echo "📋 Copiando Oracle Wallet..."
for service in k1 k2 k3; do
    if [ -d "EcommerceCloudNative_r1/Wallet_EcommerceCloudNative" ]; then
        cp -r EcommerceCloudNative_r1/Wallet_EcommerceCloudNative EcommerceCloudNative_$service/
        echo "✅ Wallet copiado a EcommerceCloudNative_$service"
    fi
done

# Construir microservicios
echo "🔨 Construyendo microservicios..."
for service in k1 k2 k3; do
    echo "Construyendo EcommerceCloudNative_$service..."
    cd EcommerceCloudNative_$service
    mvn clean package -DskipTests
    cd ..
done

# Detener servicios anteriores
echo "🛑 Deteniendo servicios anteriores..."
docker-compose -f docker-compose-kafka.yml down -v

# Iniciar servicios
echo "🚀 Iniciando servicios..."
docker-compose -f docker-compose-kafka.yml up -d

# Esperar a que Kafka esté listo
echo "⏳ Esperando a que Kafka esté listo..."
sleep 30

# Verificar servicios
echo "✅ Verificando servicios..."
docker-compose -f docker-compose-kafka.yml ps

# Mostrar logs de inicialización
echo "📋 Logs de inicialización:"
docker-compose -f docker-compose-kafka.yml logs kafka-init

echo -e "${GREEN}✅ Sistema configurado exitosamente!${NC}"
echo ""
echo "📌 URLs de acceso:"
echo "   - Kafka UI: http://localhost:8080"
echo "   - MS Ventas: http://localhost:8091"
echo "   - MS Inventario: http://localhost:8092"
echo "   - MS Promociones: http://localhost:8093"
echo ""
echo "📝 Comandos útiles:"
echo "   - Ver logs: docker-compose -f docker-compose-kafka.yml logs -f [servicio]"
echo "   - Detener: docker-compose -f docker-compose-kafka.yml down"
echo "   - Reiniciar: docker-compose -f docker-compose-kafka.yml restart"