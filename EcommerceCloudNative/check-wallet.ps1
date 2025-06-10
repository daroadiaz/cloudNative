Write-Host "Verificando Wallet de Oracle..." -ForegroundColor Yellow

$walletDir = "./Wallet_EcommerceCloudNative"

if (Test-Path $walletDir) {
    Write-Host "✓ Directorio Wallet encontrado" -ForegroundColor Green
    
    # Verificar archivos esenciales
    $files = @("cwallet.sso", "ewallet.p12", "sqlnet.ora", "tnsnames.ora")
    
    foreach ($file in $files) {
        $filePath = Join-Path $walletDir $file
        if (Test-Path $filePath) {
            Write-Host "✓ $file encontrado" -ForegroundColor Green
        } else {
            Write-Host "✗ $file NO encontrado" -ForegroundColor Red
        }
    }
} else {
    Write-Host "✗ Directorio Wallet NO encontrado" -ForegroundColor Red
    Write-Host "Por favor, asegúrate de que el Wallet esté en: $walletDir" -ForegroundColor Yellow
    exit 1
}

Write-Host "Verificación completada." -ForegroundColor Green