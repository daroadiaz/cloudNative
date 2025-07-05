# Script de pruebas automatizadas del sistema
Write-Host "=== Pruebas Automatizadas del Sistema ===" -ForegroundColor Cyan
Write-Host ""

$producer_url = "http://localhost:8081"
$consumer_url = "http://localhost:8082"
$tests_passed = 0
$tests_failed = 0

# Función para realizar pruebas
function Test-Endpoint {
    param(
        $Name,
        $Method,
        $Url,
        $Headers = @{},
        $Body = $null,
        $ExpectedStatus = 200
    )
    
    Write-Host "Probando: $Name" -ForegroundColor Yellow
    
    try {
        $params = @{
            Method = $Method
            Uri = $Url
            Headers = $Headers
            ContentType = "application/json"
        }
        
        if ($Body) {
            $params.Body = $Body
        }
        
        $response = Invoke-RestMethod @params -StatusCodeVariable statusCode -ErrorAction Stop
        
        if ($statusCode -eq $ExpectedStatus) {
            Write-Host "  ✓ Éxito (Status: $statusCode)" -ForegroundColor Green
            $script:tests_passed++
            return $response
        } else {
            Write-Host "  ✗ Error - Status inesperado: $statusCode (esperado: $ExpectedStatus)" -ForegroundColor Red
            $script:tests_failed++
            return $null
        }
    } catch {
        Write-Host "  ✗ Error: $_" -ForegroundColor Red
        $script:tests_failed++
        return $null
    }
}

# Esperar a que los servicios estén listos
Write-Host "Esperando a que los servicios estén listos..." -ForegroundColor Blue
Start-Sleep -Seconds 5

# 1. Verificar salud del Consumer
Write-Host "`n1. PRUEBAS DEL CONSUMIDOR" -ForegroundColor Blue
Test-Endpoint -Name "Health Check Consumer" `
              -Method "GET" `
              -Url "$consumer_url/api/monitor/health"

# 2. Login en Producer
Write-Host "`n2. PRUEBAS DE AUTENTICACIÓN" -ForegroundColor Blue
$loginBody = @{
    username = "Admin"
    password = "CloudNative_123"
} | ConvertTo-Json

$loginResponse = Test-Endpoint -Name "Login" `
                              -Method "POST" `
                              -Url "$producer_url/api/auth/login" `
                              -Body $loginBody

if ($loginResponse -and $loginResponse.token) {
    $token = $loginResponse.token
    Write-Host "  Token obtenido: $($token.Substring(0, 20))..." -ForegroundColor Gray
    
    $authHeaders = @{
        "Authorization" = "Bearer $token"
    }
    
    # 3. Probar endpoints del Producer
    Write-Host "`n3. PRUEBAS DEL PRODUCTOR" -ForegroundColor Blue
    
    # Health check de ventas
    Test-Endpoint -Name "Health Check Ventas" `
                  -Method "GET" `
                  -Url "$producer_url/api/ventas/health" `
                  -Headers $authHeaders
    
    # Procesar una venta
    $ventaBody = @{
        productoId = 999
        productoNombre = "Producto de Prueba"
        cantidad = 1
        precioUnitario = 100.00
        clienteNombre = "Cliente Test"
        clienteEmail = "test@test.com"
    } | ConvertTo-Json
    
    $ventaResponse = Test-Endpoint -Name "Procesar Venta" `
                                  -Method "POST" `
                                  -Url "$producer_url/api/ventas/procesar" `
                                  -Headers $authHeaders `
                                  -Body $ventaBody
    
    # Actualizar promoción
    $promoBody = @{
        codigo = "TEST2024"
        descripcion = "Promoción de prueba"
        tipoDescuento = "PORCENTAJE"
        valorDescuento = 10.0
        categoria = "TEST"
        stockLimite = 50
    } | ConvertTo-Json
    
    $promoResponse = Test-Endpoint -Name "Actualizar Promoción" `
                                  -Method "POST" `
                                  -Url "$producer_url/api/promociones/actualizar" `
                                  -Headers $authHeaders `
                                  -Body $promoBody
    
    # 4. Verificar procesamiento en Consumer
    Write-Host "`n4. VERIFICACIÓN DE PROCESAMIENTO" -ForegroundColor Blue
    Write-Host "Esperando procesamiento de mensajes..." -ForegroundColor Gray
    Start-Sleep -Seconds 3
    
    # Verificar archivos JSON generados
    $filesResponse = Test-Endpoint -Name "Listar archivos JSON" `
                                  -Method "GET" `
                                  -Url "$consumer_url/api/monitor/files"
    
    if ($filesResponse -and $filesResponse.totalFiles -gt 0) {
        Write-Host "  ✓ Se generaron $($filesResponse.totalFiles) archivos JSON" -ForegroundColor Green
    }
    
    # Verificar estadísticas
    Test-Endpoint -Name "Estadísticas del Consumer" `
                  -Method "GET" `
                  -Url "$consumer_url/api/monitor/stats"
    
} else {
    Write-Host "  ✗ No se pudo obtener el token de autenticación" -ForegroundColor Red
}

# 5. Pruebas de carga
Write-Host "`n5. PRUEBAS DE CARGA" -ForegroundColor Blue
$confirm = Read-Host "¿Ejecutar pruebas de carga? (S/N)"
if ($confirm -eq 'S' -or $confirm -eq 's') {
    if ($token) {
        Write-Host "Enviando 10 ventas..." -ForegroundColor Gray
        
        for ($i = 1; $i -le 10; $i++) {
            $ventaCarga = @{
                productoId = $i
                productoNombre = "Producto Carga $i"
                cantidad = Get-Random -Minimum 1 -Maximum 10
                precioUnitario = Get-Random -Minimum 50 -Maximum 500
                clienteNombre = "Cliente Carga $i"
                clienteEmail = "carga$i@test.com"
            } | ConvertTo-Json
            
            Test-Endpoint -Name "Venta de carga #$i" `
                         -Method "POST" `
                         -Url "$producer_url/api/ventas/procesar" `
                         -Headers $authHeaders `
                         -Body $ventaCarga | Out-Null
        }
    }
}

# Resumen
Write-Host "`n=== RESUMEN DE PRUEBAS ===" -ForegroundColor Cyan
Write-Host "Pruebas exitosas: $tests_passed" -ForegroundColor Green
Write-Host "Pruebas fallidas: $tests_failed" -ForegroundColor Red

if ($tests_failed -eq 0) {
    Write-Host "`n✓ TODAS LAS PRUEBAS PASARON EXITOSAMENTE" -ForegroundColor Green
} else {
    Write-Host "`n✗ Algunas pruebas fallaron" -ForegroundColor Red
}

# Verificar RabbitMQ Management
Write-Host "`n6. INFORMACIÓN ADICIONAL" -ForegroundColor Blue
Write-Host "RabbitMQ Management UI: http://localhost:15672" -ForegroundColor Cyan
Write-Host "Usuario: admin / Contraseña: admin123" -ForegroundColor Gray
Write-Host ""
Write-Host "Para ver los logs:" -ForegroundColor Yellow
Write-Host "  docker-compose logs -f ecommerce-producer" -ForegroundColor Gray
Write-Host "  docker-compose logs -f ecommerce-consumer" -ForegroundColor Gray
Write-Host "  docker-compose logs -f rabbitmq" -ForegroundColor Gray
Write-Host ""