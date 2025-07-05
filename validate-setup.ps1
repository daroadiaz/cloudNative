# Script de validación para verificar que todo esté configurado correctamente
Write-Host "=== Validación del Sistema de Microservicios ===" -ForegroundColor Cyan
Write-Host ""

$errors = 0
$warnings = 0

# Función de validación
function Test-FileExists {
    param($Path, $Description, $IsError = $true)
    
    if (Test-Path $Path) {
        Write-Host "✓ $Description" -ForegroundColor Green
        return $true
    } else {
        if ($IsError) {
            Write-Host "✗ $Description - NO ENCONTRADO" -ForegroundColor Red
            $script:errors++
        } else {
            Write-Host "⚠ $Description - NO ENCONTRADO" -ForegroundColor Yellow
            $script:warnings++
        }
        return $false
    }
}

# 1. Validar estructura de directorios
Write-Host "1. Validando estructura de directorios..." -ForegroundColor Blue
Test-FileExists ".\EcommerceCloudNative_r1" "Proyecto Producer (r1)"
Test-FileExists ".\EcommerceCloudNative_r2" "Proyecto Consumer (r2)"
Test-FileExists ".\docker-compose.yml" "docker-compose.yml"
Write-Host ""

# 2. Validar archivos de configuración
Write-Host "2. Validando archivos de configuración..." -ForegroundColor Blue
Test-FileExists ".\setup-rabbitmq.ps1" "Script setup-rabbitmq.ps1"
Test-FileExists ".\start-system.ps1" "Script start-system.ps1"
Test-FileExists ".\copy-required-files.ps1" "Script copy-required-files.ps1"
Write-Host ""

# 3. Validar Producer (r1)
Write-Host "3. Validando Producer (r1)..." -ForegroundColor Blue
$r1_base = ".\EcommerceCloudNative_r1"

# Archivos principales
Test-FileExists "$r1_base\pom.xml" "pom.xml"
Test-FileExists "$r1_base\Dockerfile" "Dockerfile"
Test-FileExists "$r1_base\src\main\resources\application.properties" "application.properties"
Test-FileExists "$r1_base\src\main\resources\application-docker.properties" "application-docker.properties"

# Wallet
Test-FileExists "$r1_base\Wallet_EcommerceCloudNative" "Wallet Oracle" $false

# Clases nuevas
$r1_java = "$r1_base\src\main\java\com\example\ecommerce"
Test-FileExists "$r1_java\config\RabbitMQConfig.java" "RabbitMQConfig.java"
Test-FileExists "$r1_java\controller\VentaController.java" "VentaController.java"
Test-FileExists "$r1_java\controller\PromocionController.java" "PromocionController.java"
Test-FileExists "$r1_java\model\Venta.java" "Venta.java"
Test-FileExists "$r1_java\model\Promocion.java" "Promocion.java"
Test-FileExists "$r1_java\service\RabbitMQProducerService.java" "RabbitMQProducerService.java"

# Verificar que no existan archivos antiguos
Write-Host "`n  Verificando archivos eliminados..." -ForegroundColor Gray
if (Test-Path "$r1_java\controller\InstrumentoController.java") {
    Write-Host "  ⚠ InstrumentoController.java aún existe (debe eliminarse)" -ForegroundColor Yellow
    $warnings++
}
if (Test-Path "$r1_java\config\DataInitializer.java") {
    Write-Host "  ⚠ DataInitializer.java aún existe (debe eliminarse)" -ForegroundColor Yellow
    $warnings++
}
Write-Host ""

# 4. Validar Consumer (r2)
Write-Host "4. Validando Consumer (r2)..." -ForegroundColor Blue
$r2_base = ".\EcommerceCloudNative_r2"

# Archivos principales
Test-FileExists "$r2_base\pom.xml" "pom.xml"
Test-FileExists "$r2_base\Dockerfile" "Dockerfile"
Test-FileExists "$r2_base\src\main\resources\application.properties" "application.properties"
Test-FileExists "$r2_base\src\main\resources\application-docker.properties" "application-docker.properties"

# Wallet
Test-FileExists "$r2_base\Wallet_EcommerceCloudNative" "Wallet Oracle" $false

# Clases nuevas
$r2_java = "$r2_base\src\main\java\com\example\ecommerce"
Test-FileExists "$r2_java\config\RabbitMQConfig.java" "RabbitMQConfig.java"
Test-FileExists "$r2_java\controller\MonitorController.java" "MonitorController.java"
Test-FileExists "$r2_java\service\SalesConsumerService.java" "SalesConsumerService.java"
Test-FileExists "$r2_java\service\PromotionsConsumerService.java" "PromotionsConsumerService.java"
Test-FileExists "$r2_java\model\Venta.java" "Venta.java (copiado de r1)"
Test-FileExists "$r2_java\model\Promocion.java" "Promocion.java (copiado de r1)"

# Verificar que no existan archivos antiguos
Write-Host "`n  Verificando archivos eliminados..." -ForegroundColor Gray
if (Test-Path "$r2_java\controller\AuthController.java") {
    Write-Host "  ⚠ AuthController.java aún existe (debe eliminarse)" -ForegroundColor Yellow
    $warnings++
}
if (Test-Path "$r2_java\security") {
    Write-Host "  ⚠ Carpeta security aún existe (debe eliminarse)" -ForegroundColor Yellow
    $warnings++
}
Write-Host ""

# 5. Validar Docker
Write-Host "5. Validando Docker..." -ForegroundColor Blue
try {
    docker --version | Out-Null
    Write-Host "✓ Docker instalado" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker no está instalado o no está en el PATH" -ForegroundColor Red
    $errors++
}

try {
    docker-compose --version | Out-Null
    Write-Host "✓ Docker Compose instalado" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker Compose no está instalado o no está en el PATH" -ForegroundColor Red
    $errors++
}
Write-Host ""

# 6. Validar Maven
Write-Host "6. Validando Maven..." -ForegroundColor Blue
try {
    mvn --version | Out-Null
    Write-Host "✓ Maven instalado" -ForegroundColor Green
} catch {
    Write-Host "✗ Maven no está instalado o no está en el PATH" -ForegroundColor Red
    $errors++
}
Write-Host ""

# 7. Validar directorios de salida
Write-Host "7. Validando directorios de salida..." -ForegroundColor Blue
Test-FileExists ".\output" "Directorio output" $false
Test-FileExists ".\logs" "Directorio logs" $false
Write-Host ""

# Resumen
Write-Host "=== Resumen de Validación ===" -ForegroundColor Cyan
Write-Host "Errores: $errors" -ForegroundColor $(if ($errors -eq 0) { "Green" } else { "Red" })
Write-Host "Advertencias: $warnings" -ForegroundColor $(if ($warnings -eq 0) { "Green" } else { "Yellow" })

if ($errors -eq 0) {
    Write-Host "`n✓ El sistema está listo para ejecutarse" -ForegroundColor Green
    Write-Host "  Ejecuta: .\start-system.ps1" -ForegroundColor Gray
} else {
    Write-Host "`n✗ Hay errores que deben corregirse antes de ejecutar el sistema" -ForegroundColor Red
}

if ($warnings -gt 0) {
    Write-Host "`n⚠ Hay advertencias que deberías revisar" -ForegroundColor Yellow
}

Write-Host ""