# Script de limpieza automática para preparar r1 y r2
Write-Host "=== Script de Limpieza de Proyectos ===" -ForegroundColor Cyan
Write-Host ""

# Función para eliminar archivos con confirmación
function Remove-FileWithConfirmation {
    param($Path, $Description)
    
    if (Test-Path $Path) {
        Write-Host "  Eliminando: $Description" -ForegroundColor Yellow
        Remove-Item $Path -Force -Recurse -ErrorAction SilentlyContinue
        Write-Host "  ✓ Eliminado" -ForegroundColor Green
    } else {
        Write-Host "  ⚠ No encontrado: $Description" -ForegroundColor Gray
    }
}

# Confirmación antes de proceder
Write-Host "Este script eliminará archivos innecesarios de r1 y r2" -ForegroundColor Red
$confirm = Read-Host "¿Deseas continuar? (S/N)"
if ($confirm -ne 'S' -and $confirm -ne 's') {
    Write-Host "Operación cancelada" -ForegroundColor Yellow
    exit
}

# Limpieza de EcommerceCloudNative_r1
Write-Host "`n1. Limpiando EcommerceCloudNative_r1..." -ForegroundColor Blue
$r1_base = ".\EcommerceCloudNative_r1\src\main\java\com\example\ecommerce"

Remove-FileWithConfirmation "$r1_base\controller\InstrumentoController.java" "InstrumentoController.java"
Remove-FileWithConfirmation "$r1_base\service\InstrumentoService.java" "InstrumentoService.java"
Remove-FileWithConfirmation "$r1_base\repository\InstrumentoRepository.java" "InstrumentoRepository.java"
Remove-FileWithConfirmation "$r1_base\model\Instrumento.java" "Instrumento.java"
Remove-FileWithConfirmation "$r1_base\config\DataInitializer.java" "DataInitializer.java"
Remove-FileWithConfirmation "$r1_base\config\SecurityPasswordLogger.java" "SecurityPasswordLogger.java"

Write-Host "✓ Limpieza de r1 completada" -ForegroundColor Green

# Limpieza de EcommerceCloudNative_r2
Write-Host "`n2. Limpiando EcommerceCloudNative_r2..." -ForegroundColor Blue
$r2_base = ".\EcommerceCloudNative_r2\src\main\java\com\example\ecommerce"

Remove-FileWithConfirmation "$r2_base\controller\InstrumentoController.java" "InstrumentoController.java"
Remove-FileWithConfirmation "$r2_base\controller\AuthController.java" "AuthController.java"
Remove-FileWithConfirmation "$r2_base\service\InstrumentoService.java" "InstrumentoService.java"
Remove-FileWithConfirmation "$r2_base\repository\InstrumentoRepository.java" "InstrumentoRepository.java"
Remove-FileWithConfirmation "$r2_base\model\Instrumento.java" "Instrumento.java"
Remove-FileWithConfirmation "$r2_base\security" "Carpeta security completa"
Remove-FileWithConfirmation "$r2_base\dto" "Carpeta dto completa"
Remove-FileWithConfirmation "$r2_base\config\DataInitializer.java" "DataInitializer.java"
Remove-FileWithConfirmation "$r2_base\config\SecurityPasswordLogger.java" "SecurityPasswordLogger.java"

Write-Host "✓ Limpieza de r2 completada" -ForegroundColor Green

# Crear directorios necesarios
Write-Host "`n3. Creando directorios necesarios..." -ForegroundColor Blue
$directories = @(
    ".\output\promociones",
    ".\logs\producer",
    ".\logs\consumer"
)

foreach ($dir in $directories) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
        Write-Host "  ✓ Creado: $dir" -ForegroundColor Green
    }
}

# Resumen
Write-Host "`n=== Limpieza Completada ===" -ForegroundColor Green
Write-Host ""
Write-Host "Próximos pasos:" -ForegroundColor Yellow
Write-Host "1. Crear los archivos nuevos en cada proyecto" -ForegroundColor Gray
Write-Host "2. Copiar los modelos de r1 a r2 con: .\copy-required-files.ps1" -ForegroundColor Gray
Write-Host "3. Compilar ambos proyectos" -ForegroundColor Gray
Write-Host "4. Ejecutar el sistema con: .\start-system.ps1" -ForegroundColor Gray
Write-Host ""