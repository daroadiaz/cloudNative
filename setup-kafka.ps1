Write-Host "🚀 Configurando sistema de microservicios con Kafka..." -ForegroundColor Green

# Verificar Docker
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Docker no está instalado" -ForegroundColor Red
    exit 1
}

# Verificar Docker Compose
if (-not (Get-Command docker-compose -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Docker Compose no está instalado" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Docker y Docker Compose detectados" -ForegroundColor Green

# Crear directorios
Write-Host "📁 Creando directorios..." -ForegroundColor Yellow
New-Item -ItemType Directory -Force -Path "logs\ventas"
New-Item -ItemType Directory -Force -Path "logs\inventario"
New-Item -ItemType Directory -Force -Path "logs\promociones"

# Copiar wallet
Write-Host "📋 Copiando Oracle Wallet..." -ForegroundColor Yellow
foreach ($service in @("k1", "k2", "k3")) {
    if (Test-Path "EcommerceCloudNative_r1\Wallet_EcommerceCloudNative") {
        Copy-Item -Path "EcommerceCloudNative_r1\Wallet_EcommerceCloudNative" `
                  -Destination "EcommerceCloudNative_$service\" -Recurse -Force
        Write-Host "✅ Wallet copiado a EcommerceCloudNative_$service" -ForegroundColor Green
    }
}

# Construir microservicios
Write-Host "🔨 Construyendo microservicios..." -ForegroundColor Yellow
foreach ($service in @("k1", "k2", "k3")) {
    Write-Host "Construyendo EcommerceCloudNative_$service..." -ForegroundColor Cyan
    Set-Location "EcommerceCloudNative_$service"
    mvn clean package -DskipTests
    Set-Location ..
}

# Detener servicios anteriores
Write-Host "🛑 Deteniendo servicios anteriores..." -ForegroundColor Yellow
docker-compose -f docker-compose-kafka.yml down -v

# Iniciar servicios
Write-Host "🚀 Iniciando servicios..." -ForegroundColor Yellow
docker-compose -f docker-compose-kafka.yml up -d

# Esperar
Write-Host "⏳ Esperando a que Kafka esté listo..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Verificar servicios
Write-Host "✅ Verificando servicios..." -ForegroundColor Green
docker-compose -f docker-compose-kafka.yml ps

# Mostrar logs
Write-Host "📋 Logs de inicialización:" -ForegroundColor Yellow
docker-compose -f docker-compose-kafka.yml logs kafka-init

Write-Host "`n✅ Sistema configurado exitosamente!" -ForegroundColor Green
Write-Host "`n📌 URLs de acceso:" -ForegroundColor Cyan
Write-Host "   - Kafka UI: http://localhost:8080"
Write-Host "   - MS Ventas: http://localhost:8091"
Write-Host "   - MS Inventario: http://localhost:8092"
Write-Host "   - MS Promociones: http://localhost:8093"
Write-Host "`n📝 Comandos útiles:" -ForegroundColor Cyan
Write-Host "   - Ver logs: docker-compose -f docker-compose-kafka.yml logs -f [servicio]"
Write-Host "   - Detener: docker-compose -f docker-compose-kafka.yml down"
Write-Host "   - Reiniciar: docker-compose -f docker-compose-kafka.yml restart"