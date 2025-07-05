@echo off
echo 🚀 Iniciando proceso de despliegue...

REM Verificar que existe el wallet de Oracle
if not exist "Wallet_EcommerceCloudNative" (
    echo ❌ Error: No se encontro el directorio Wallet_EcommerceCloudNative
    echo Por favor, asegurate de copiar el wallet en el directorio raiz del proyecto
    exit /b 1
)

REM Limpiar contenedores anteriores
echo 🧹 Limpiando contenedores anteriores...
docker-compose down 2>nul

REM Construir la imagen
echo 🔨 Construyendo imagen Docker...
docker build -t ecommerce-api:latest .

REM Verificar si la construccion fue exitosa
if %errorlevel% neq 0 (
    echo ❌ Error al construir la imagen
    exit /b 1
)

echo ✅ Imagen construida exitosamente

REM Crear directorio de logs si no existe
if not exist "logs" mkdir logs

REM Iniciar el contenedor con docker-compose
echo 🎯 Iniciando contenedor...
docker-compose up -d

REM Esperar a que el servicio este listo
echo ⏳ Esperando a que el servicio inicie...
timeout /t 10 /nobreak >nul

REM Verificar el estado del contenedor
docker ps | findstr "ecommerce-springboot" >nul
if %errorlevel% equ 0 (
    echo ✅ Contenedor iniciado correctamente
    echo 📋 Logs del contenedor:
    docker logs --tail 20 ecommerce-springboot
    echo.
    echo 🌐 La aplicacion esta disponible en: http://localhost:8080
    echo 🔐 Usuario: Admin
    echo 🔑 Contraseña: CloudNative_123
) else (
    echo ❌ Error: El contenedor no esta ejecutandose
    docker logs ecommerce-springboot
    exit /b 1
)