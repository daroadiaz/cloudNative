# Script de despliegue para Windows PowerShell

Write-Host "🚀 Iniciando proceso de despliegue..." -ForegroundColor Green

# Verificar que existe el wallet de Oracle
if (-not (Test-Path "Wallet_EcommerceCloudNative")) {
    Write-Host "❌ Error: No se encontró el directorio Wallet_EcommerceCloudNative" -ForegroundColor Red
    Write-Host "Por favor, asegúrate de copiar el wallet en el directorio raíz del proyecto" -ForegroundColor Yellow
    exit 1
}

# Limpiar contenedores anteriores
Write-Host "🧹 Limpiando contenedores anteriores..." -ForegroundColor Yellow
docker-compose down 2>$null

# Construir la imagen
Write-Host "🔨 Construyendo imagen Docker..." -ForegroundColor Yellow
docker build -t ecommerce-api:latest .

# Verificar si la construcción fue exitosa
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Imagen construida exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error al construir la imagen" -ForegroundColor Red
    exit 1
}

# Crear directorio de logs si no existe
if (-not (Test-Path "logs")) {
    New-Item -ItemType Directory -Path "logs" | Out-Null
}

# Iniciar el contenedor con docker-compose
Write-Host "🎯 Iniciando contenedor..." -ForegroundColor Yellow
docker-compose up -d

# Esperar a que el servicio esté listo
Write-Host "⏳ Esperando a que el servicio inicie..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Verificar el estado del contenedor
$containerRunning = docker ps | Select-String "ecommerce-springboot"
if ($containerRunning) {
    Write-Host "✅ Contenedor iniciado correctamente" -ForegroundColor Green
    Write-Host "📋 Logs del contenedor:" -ForegroundColor Cyan
    docker logs --tail 20 ecommerce-springboot
    Write-Host ""
    Write-Host "🌐 La aplicación está disponible en: http://localhost:8080" -ForegroundColor Green
    Write-Host "🔐 Usuario: Admin" -ForegroundColor Cyan
    Write-Host "🔑 Contraseña: CloudNative_123" -ForegroundColor Cyan
} else {
    Write-Host "❌ Error: El contenedor no está ejecutándose" -ForegroundColor Red
    docker logs ecommerce-springboot
    exit 1
}