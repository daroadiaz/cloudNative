# Script para copiar archivos necesarios del proyecto original a r1 y r2
Write-Host "=== Copiando archivos necesarios ===" -ForegroundColor Cyan

# Copiar modelos de r1 a r2
Write-Host "Copiando modelos de r1 a r2..." -ForegroundColor Blue

$modelPath_r1 = ".\EcommerceCloudNative_r1\src\main\java\com\example\ecommerce\model"
$modelPath_r2 = ".\EcommerceCloudNative_r2\src\main\java\com\example\ecommerce\model"

# Crear directorio si no existe
if (-not (Test-Path $modelPath_r2)) {
    New-Item -ItemType Directory -Path $modelPath_r2 -Force
}

# Copiar Venta.java y Promocion.java
Copy-Item "$modelPath_r1\Venta.java" -Destination $modelPath_r2 -Force
Copy-Item "$modelPath_r1\Promocion.java" -Destination $modelPath_r2 -Force

Write-Host "✓ Modelos copiados exitosamente" -ForegroundColor Green

# Copiar repositorio VentaRepository.java a r2
$repoPath_r1 = ".\EcommerceCloudNative_r1\src\main\java\com\example\ecommerce\repository"
$repoPath_r2 = ".\EcommerceCloudNative_r2\src\main\java\com\example\ecommerce\repository"

if (-not (Test-Path $repoPath_r2)) {
    New-Item -ItemType Directory -Path $repoPath_r2 -Force
}

Copy-Item "$repoPath_r1\VentaRepository.java" -Destination $repoPath_r2 -Force

Write-Host "✓ Repositorio copiado exitosamente" -ForegroundColor Green

# Crear directorios de output para JSON
Write-Host "Creando directorios de output..." -ForegroundColor Blue

$outputPaths = @(
    ".\output\promociones",
    ".\logs\producer",
    ".\logs\consumer"
)

foreach ($path in $outputPaths) {
    if (-not (Test-Path $path)) {
        New-Item -ItemType Directory -Path $path -Force
        Write-Host "  ✓ Creado: $path" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "=== Archivos copiados exitosamente ===" -ForegroundColor Green