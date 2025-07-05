#!/bin/bash

echo "=== Configuración de RabbitMQ para Ecommerce ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Paso 1: Levantar RabbitMQ con docker-compose
echo -e "${BLUE}1. Levantando RabbitMQ con docker-compose...${NC}"
docker-compose up -d rabbitmq

# Esperar a que RabbitMQ esté listo
echo -e "${BLUE}2. Esperando a que RabbitMQ esté listo...${NC}"
sleep 10

# Verificar que RabbitMQ esté corriendo
until docker exec ecommerce-rabbitmq rabbitmqctl status > /dev/null 2>&1; do
    echo "Esperando a RabbitMQ..."
    sleep 5
done

echo -e "${GREEN}✓ RabbitMQ está listo${NC}"

# Paso 2: Crear exchange usando rabbitmqadmin
echo -e "${BLUE}3. Creando exchange...${NC}"
docker exec ecommerce-rabbitmq rabbitmqadmin declare exchange \
    name=ecommerce-exchange \
    type=direct \
    durable=true

# Paso 3: Crear colas
echo -e "${BLUE}4. Creando colas...${NC}"
docker exec ecommerce-rabbitmq rabbitmqadmin declare queue \
    name=sales-queue \
    durable=true

docker exec ecommerce-rabbitmq rabbitmqadmin declare queue \
    name=promotions-queue \
    durable=true

# Paso 4: Crear bindings
echo -e "${BLUE}5. Creando bindings...${NC}"
docker exec ecommerce-rabbitmq rabbitmqadmin declare binding \
    source=ecommerce-exchange \
    destination=sales-queue \
    routing_key=sales.routing.key

docker exec ecommerce-rabbitmq rabbitmqadmin declare binding \
    source=ecommerce-exchange \
    destination=promotions-queue \
    routing_key=promotions.routing.key

# Verificar configuración
echo -e "${BLUE}6. Verificando configuración...${NC}"
echo -e "${GREEN}Exchanges:${NC}"
docker exec ecommerce-rabbitmq rabbitmqadmin list exchanges name type

echo -e "${GREEN}Queues:${NC}"
docker exec ecommerce-rabbitmq rabbitmqadmin list queues name messages

echo -e "${GREEN}Bindings:${NC}"
docker exec ecommerce-rabbitmq rabbitmqadmin list bindings source destination routing_key

echo ""
echo -e "${GREEN}=== Configuración completada ===${NC}"
echo -e "Management UI disponible en: http://localhost:15672"
echo -e "Usuario: admin"
echo -e "Contraseña: admin123"