# Script para iniciar el sistema completo de Ecommerce con RabbitMQ
Write-Host "=== Iniciando Sistema de Ecommerce con RabbitMQ ===" -ForegroundColor Cyan
Write-Host ""

# Función para verificar si un puerto está en uso
function Test-Port {
    param($Port)
    $connection = New-Object System.Net.Sockets.TcpClient
    try {
        $connection.Connect("localhost", $Port)
        $connection.Close()
        return $true
    } catch {
        return $false
    }
}

# Paso 1: Verificar puertos
Write-Host "1. Verificando disponibilidad de puertos..." -ForegroundColor Blue
$ports = @(5672, 15672, 8081, 8082)
$portsOk = $true

foreach ($port in $ports) {
    if (Test-Port -Port $port) {
        Write-Host "   Puerto $port está en uso" -ForegroundColor Red
        $portsOk = $false
    } else {
        Write-Host "   Puerto $port disponible" -ForegroundColor Green
    }
}

if (-not $portsOk) {
    Write-Host "Por favor, libera los puertos en uso antes de continuar" -ForegroundColor Red
    exit 1
}

# Paso 2: Compilar proyectos
Write-Host "`n2. Compilando proyectos..." -ForegroundColor Blue

Write-Host "   Compilando Producer (r1)..." -ForegroundColor Yellow
Set-Location -Path ".\EcommerceCloudNative_r1"
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Error compilando Producer" -ForegroundColor Red
    exit 1
}

Write-Host "   Compilando Consumer (r2)..." -ForegroundColor Yellow
Set-Location -Path "..\EcommerceCloudNative_r2"
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Error compilando Consumer" -ForegroundColor Red
    exit 1
}

Set-Location -Path ".."

# Paso 3: Iniciar servicios con docker-compose
Write-Host "`n3. Iniciando servicios con docker-compose..." -ForegroundColor Blue
docker-compose up -d

# Esperar a que RabbitMQ esté listo
Write-Host "`n4. Esperando a que RabbitMQ esté listo..." -ForegroundColor Blue
$maxAttempts = 30
$attempt = 0
$ready = $false

while (($attempt -lt $maxAttempts) -and (-not $ready)) {
    $attempt++
    try {
        docker exec ecommerce-rabbitmq rabbitmqctl status 2>&1 | Out-Null
        $ready = $true
    } catch {
        Write-Host "   Intento $attempt de $maxAttempts..."
        Start-Sleep -Seconds 2
    }
}

if (-not $ready) {
    Write-Host "RabbitMQ no pudo iniciarse correctamente" -ForegroundColor Red
    exit 1
}

Write-Host "✓ RabbitMQ está listo" -ForegroundColor Green

# Paso 4: Configurar RabbitMQ
Write-Host "`n5. Configurando colas y exchanges en RabbitMQ..." -ForegroundColor Blue

# Crear exchange
docker exec ecommerce-rabbitmq rabbitmqadmin declare exchange `
    name=ecommerce-exchange `
    type=direct `
    durable=true

# Crear colas
docker exec ecommerce-rabbitmq rabbitmqadmin declare queue `
    name=sales-queue `
    durable=true

docker exec ecommerce-rabbitmq rabbitmqadmin declare queue `
    name=promotions-queue `
    durable=true

# Crear bindings
docker exec ecommerce-rabbitmq rabbitmqadmin declare binding `
    source=ecommerce-exchange `
    destination=sales-queue `
    routing_key=sales.routing.key

docker exec ecommerce-rabbitmq rabbitmqadmin declare binding `
    source=ecommerce-exchange `
    destination=promotions-queue `
    routing_key=promotions.routing.key

Write-Host "✓ RabbitMQ configurado correctamente" -ForegroundColor Green

# Paso 5: Verificar estado de todos los servicios
Write-Host "`n6. Verificando estado de los servicios..." -ForegroundColor Blue
docker-compose ps

# Mostrar URLs de acceso
Write-Host "`n=== Sistema iniciado correctamente ===" -ForegroundColor Green
Write-Host ""
Write-Host "URLs de acceso:" -ForegroundColor Yellow
Write-Host "  RabbitMQ Management: http://localhost:15672" -ForegroundColor Cyan
Write-Host "    Usuario: admin" -ForegroundColor Gray
Write-Host "    Contraseña: admin123" -ForegroundColor Gray
Write-Host ""
Write-Host "  Producer API (r1): http://localhost:8081" -ForegroundColor Cyan
Write-Host "    - POST /api/auth/login" -ForegroundColor Gray
Write-Host "    - POST /api/ventas/procesar" -ForegroundColor Gray
Write-Host "    - POST /api/promociones/actualizar" -ForegroundColor Gray
Write-Host ""
Write-Host "  Consumer API (r2): http://localhost:8082" -ForegroundColor Cyan
Write-Host "    - GET /api/monitor/health" -ForegroundColor Gray
Write-Host "    - GET /api/monitor/files" -ForegroundColor Gray
Write-Host "    - GET /api/monitor/stats" -ForegroundColor Gray
Write-Host ""
Write-Host "Para ver los logs de los servicios:" -ForegroundColor Yellow
Write-Host "  docker-compose logs -f rabbitmq" -ForegroundColor Gray
Write-Host "  docker-compose logs -f ecommerce-producer" -ForegroundColor Gray
Write-Host "  docker-compose logs -f ecommerce-consumer" -ForegroundColor Gray
Write-Host ""
Write-Host "Para detener el sistema:" -ForegroundColor Yellow
Write-Host "  docker-compose down" -ForegroundColor Gray
Write-Host ""