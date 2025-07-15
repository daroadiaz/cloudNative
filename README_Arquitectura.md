# 📂 GUÍA COMPLETA DE UBICACIÓN DE ARCHIVOS

## 🗂️ Archivos en el Directorio Raíz
**Ubicación:** `C:\Users\esnup\Desktop\entregableCloud\cloudNative\`

```
cloudNative/
├── docker-compose.yml
├── setup-rabbitmq.ps1
├── setup-rabbitmq.sh
├── start-system.ps1
├── cleanup-projects.ps1
├── copy-required-files.ps1
├── validate-setup.ps1
├── test-system.ps1
├── curl-commands.sh
├── .gitignore
├── README.md
├── TROUBLESHOOTING.md
├── ARCHIVOS-A-ELIMINAR.md
├── PASOS-IMPLEMENTACION.md
├── RESUMEN-SISTEMA.md
├── Ecommerce-RabbitMQ-Collection.json
├── EcommerceCloudNative_r1/
└── EcommerceCloudNative_r2/
```

---

## 📁 PROYECTO 1: EcommerceCloudNative_r1 (PRODUCER)

### 🔧 Archivos de Configuración Principal

```
EcommerceCloudNative_r1/
├── pom.xml (MODIFICAR - reemplazar completamente)
├── Dockerfile (CREAR NUEVO)
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│           ├── application.properties (MODIFICAR)
│           └── application-docker.properties (MODIFICAR)
```

### ☕ Archivos Java - Estructura Completa

```
EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/
├── EcommerceApplication.java (MANTENER sin cambios)
│
├── config/
│   ├── CorsConfig.java (MANTENER sin cambios)
│   ├── RabbitMQConfig.java (CREAR NUEVO) ✨
│   └── SecurityConfig.java (MANTENER sin cambios)
│
├── controller/
│   ├── AuthController.java (MANTENER sin cambios)
│   ├── PromocionController.java (CREAR NUEVO) ✨
│   └── VentaController.java (CREAR NUEVO) ✨
│
├── dto/
│   ├── LoginRequest.java (MANTENER sin cambios)
│   └── LoginResponse.java (MANTENER sin cambios)
│
├── model/
│   ├── Promocion.java (CREAR NUEVO) ✨
│   └── Venta.java (CREAR NUEVO) ✨
│
├── repository/
│   ├── PromocionRepository.java (CREAR NUEVO) ✨
│   └── VentaRepository.java (CREAR NUEVO) ✨
│
├── security/
│   ├── JwtAuthenticationEntryPoint.java (MANTENER sin cambios)
│   ├── JwtAuthenticationFilter.java (MANTENER sin cambios)
│   └── JwtUtil.java (MANTENER sin cambios)
│
└── service/
    └── RabbitMQProducerService.java (CREAR NUEVO) ✨
```

### 🗑️ Archivos a ELIMINAR en r1:
- ❌ `controller/InstrumentoController.java`
- ❌ `model/Instrumento.java`
- ❌ `repository/InstrumentoRepository.java`
- ❌ `service/InstrumentoService.java`
- ❌ `config/DataInitializer.java`
- ❌ `config/SecurityPasswordLogger.java`

---

## 📁 PROYECTO 2: EcommerceCloudNative_r2 (CONSUMER)

### 🔧 Archivos de Configuración Principal

```
EcommerceCloudNative_r2/
├── pom.xml (MODIFICAR - reemplazar completamente)
├── Dockerfile (CREAR NUEVO)
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│           ├── application.properties (MODIFICAR)
│           └── application-docker.properties (MODIFICAR)
```

### ☕ Archivos Java - Estructura Completa

```
EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/
├── EcommerceApplication.java (MANTENER sin cambios)
│
├── config/
│   ├── CorsConfig.java (CREAR NUEVO) ✨
│   ├── RabbitMQConfig.java (CREAR NUEVO) ✨
│   └── SecurityConfig.java (MODIFICAR - versión simplificada) 🔄
│
├── controller/
│   └── MonitorController.java (CREAR NUEVO) ✨
│
├── model/
│   ├── Promocion.java (COPIAR de r1) 📋
│   └── Venta.java (COPIAR de r1) 📋
│
├── repository/
│   └── VentaRepository.java (COPIAR de r1) 📋
│
└── service/
    ├── PromotionsConsumerService.java (CREAR NUEVO) ✨
    └── SalesConsumerService.java (CREAR NUEVO) ✨
```

### 🗑️ Archivos a ELIMINAR en r2:
- ❌ `controller/InstrumentoController.java`
- ❌ `controller/AuthController.java`
- ❌ `model/Instrumento.java`
- ❌ `repository/InstrumentoRepository.java`
- ❌ `service/InstrumentoService.java`
- ❌ `config/DataInitializer.java`
- ❌ `config/SecurityPasswordLogger.java`
- ❌ TODA la carpeta `security/` (con todos sus archivos)
- ❌ TODA la carpeta `dto/` (con todos sus archivos)

---

## 📋 TABLA RESUMEN DE ACCIONES

### Para EcommerceCloudNative_r1:

| Archivo | Acción | Ubicación Completa |
|---------|--------|-------------------|
| pom.xml | MODIFICAR | `/EcommerceCloudNative_r1/pom.xml` |
| Dockerfile | CREAR | `/EcommerceCloudNative_r1/Dockerfile` |
| application.properties | MODIFICAR | `/EcommerceCloudNative_r1/src/main/resources/application.properties` |
| application-docker.properties | MODIFICAR | `/EcommerceCloudNative_r1/src/main/resources/application-docker.properties` |
| RabbitMQConfig.java | CREAR | `/EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/config/RabbitMQConfig.java` |
| Venta.java | CREAR | `/EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/model/Venta.java` |
| Promocion.java | CREAR | `/EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/model/Promocion.java` |
| VentaRepository.java | CREAR | `/EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/repository/VentaRepository.java` |
| PromocionRepository.java | CREAR | `/EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/repository/PromocionRepository.java` |
| RabbitMQProducerService.java | CREAR | `/EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/service/RabbitMQProducerService.java` |
| VentaController.java | CREAR | `/EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/controller/VentaController.java` |
| PromocionController.java | CREAR | `/EcommerceCloudNative_r1/src/main/java/com/example/ecommerce/controller/PromocionController.java` |

### Para EcommerceCloudNative_r2:

| Archivo | Acción | Ubicación Completa |
|---------|--------|-------------------|
| pom.xml | MODIFICAR | `/EcommerceCloudNative_r2/pom.xml` |
| Dockerfile | CREAR | `/EcommerceCloudNative_r2/Dockerfile` |
| application.properties | MODIFICAR | `/EcommerceCloudNative_r2/src/main/resources/application.properties` |
| application-docker.properties | MODIFICAR | `/EcommerceCloudNative_r2/src/main/resources/application-docker.properties` |
| SecurityConfig.java | MODIFICAR | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/config/SecurityConfig.java` |
| CorsConfig.java | CREAR | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/config/CorsConfig.java` |
| RabbitMQConfig.java | CREAR | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/config/RabbitMQConfig.java` |
| MonitorController.java | CREAR | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/controller/MonitorController.java` |
| SalesConsumerService.java | CREAR | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/service/SalesConsumerService.java` |
| PromotionsConsumerService.java | CREAR | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/service/PromotionsConsumerService.java` |
| Venta.java | COPIAR de r1 | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/model/Venta.java` |
| Promocion.java | COPIAR de r1 | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/model/Promocion.java` |
| VentaRepository.java | COPIAR de r1 | `/EcommerceCloudNative_r2/src/main/java/com/example/ecommerce/repository/VentaRepository.java` |

---

## 🛠️ COMANDOS PARA CREAR DIRECTORIOS

### PowerShell - Crear estructura en r1:
```powershell
# Asegurarse de que existan todos los directorios
cd EcommerceCloudNative_r1
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\config"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\controller"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\dto"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\model"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\repository"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\security"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\service"
cd ..
```

### PowerShell - Crear estructura en r2:
```powershell
# Asegurarse de que existan todos los directorios
cd EcommerceCloudNative_r2
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\config"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\controller"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\model"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\repository"
New-Item -ItemType Directory -Force -Path "src\main\java\com\example\ecommerce\service"
cd ..
```

---

## 📌 NOTAS IMPORTANTES

1. **Leyenda de iconos:**
   - ✨ = Archivo NUEVO a crear
   - 🔄 = Archivo a MODIFICAR
   - 📋 = Archivo a COPIAR de otro proyecto
   - ❌ = Archivo a ELIMINAR

2. **Orden de implementación recomendado:**
   1. Primero ejecutar `cleanup-projects.ps1` para limpiar
   2. Crear todos los archivos nuevos en r1
   3. Compilar y verificar r1
   4. Crear archivos en r2
   5. Ejecutar `copy-required-files.ps1` para copiar modelos
   6. Compilar y verificar r2

3. **Verificación:**
   - Usa `validate-setup.ps1` para verificar que todo esté en su lugar
   - Revisa que no queden archivos del proyecto original (Instrumento)

4. **El Wallet de Oracle debe estar en:**
   - `/EcommerceCloudNative_r1/Wallet_EcommerceCloudNative/`
   - `/EcommerceCloudNative_r2/Wallet_EcommerceCloudNative/`