# Script de configuración de RabbitMQ para Ecommerce
Write-Host "=== Configuración de RabbitMQ para Ecommerce ===" -ForegroundColor Cyan
Write-Host ""

# Paso 1: Levantar RabbitMQ con docker-compose
Write-Host "1. Levantando RabbitMQ con docker-compose..." -ForegroundColor Blue
docker-compose up -d rabbitmq

# Esperar a que RabbitMQ esté listo
Write-Host "2. Esperando a que RabbitMQ esté listo..." -ForegroundColor Blue
Start-Sleep -Seconds 10

# Verificar que RabbitMQ esté corriendo
$ready = $false
while (-not $ready) {
    try {
        docker exec ecommerce-rabbitmq rabbitmqctl status 2>&1 | Out-Null
        $ready = $true
    } catch {
        Write-Host "Esperando a RabbitMQ..."
        Start-Sleep -Seconds 5
    }
}

Write-Host "✓ RabbitMQ está listo" -ForegroundColor Green

# Paso 2: Crear exchange
Write-Host "3. Creando exchange..." -ForegroundColor Blue
docker exec ecommerce-rabbitmq rabbitmqadmin declare exchange `
    name=ecommerce-exchange `
    type=direct `
    durable=true

# Paso 3: Crear colas
Write-Host "4. Creando colas..." -ForegroundColor Blue
docker exec ecommerce-rabbitmq rabbitmqadmin declare queue `
    name=sales-queue `
    durable=true

docker exec ecommerce-rabbitmq rabbitmqadmin declare queue `
    name=promotions-queue `
    durable=true

# Paso 4: Crear bindings
Write-Host "5. Creando bindings..." -ForegroundColor Blue
docker exec ecommerce-rabbitmq rabbitmqadmin declare binding `
    source=ecommerce-exchange `
    destination=sales-queue `
    routing_key=sales.routing.key

docker exec ecommerce-rabbitmq rabbitmqadmin declare binding `
    source=ecommerce-exchange `
    destination=promotions-queue `
    routing_key=promotions.routing.key

# Verificar configuración
Write-Host "6. Verificando configuración..." -ForegroundColor Blue
Write-Host "Exchanges:" -ForegroundColor Green
docker exec ecommerce-rabbitmq rabbitmqadmin list exchanges name type

Write-Host "`nQueues:" -ForegroundColor Green
docker exec ecommerce-rabbitmq rabbitmqadmin list queues name messages

Write-Host "`nBindings:" -ForegroundColor Green
docker exec ecommerce-rabbitmq rabbitmqadmin list bindings source destination routing_key

Write-Host ""
Write-Host "=== Configuración completada ===" -ForegroundColor Green
Write-Host "Management UI disponible en: http://localhost:15672" -ForegroundColor Yellow
Write-Host "Usuario: admin" -ForegroundColor Yellow
Write-Host "Contraseña: admin123" -ForegroundColor Yellow