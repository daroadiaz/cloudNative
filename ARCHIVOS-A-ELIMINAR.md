# Guía de Archivos a Eliminar

## 📁 EcommerceCloudNative_r1 (Producer)

### Archivos a ELIMINAR:

#### Controladores
- ❌ `src/main/java/com/example/ecommerce/controller/InstrumentoController.java`

#### Servicios
- ❌ `src/main/java/com/example/ecommerce/service/InstrumentoService.java`

#### Repositorios
- ❌ `src/main/java/com/example/ecommerce/repository/InstrumentoRepository.java`

#### Modelos
- ❌ `src/main/java/com/example/ecommerce/model/Instrumento.java`

#### Configuración
- ❌ `src/main/java/com/example/ecommerce/config/DataInitializer.java`
- ❌ `src/main/java/com/example/ecommerce/config/SecurityPasswordLogger.java`

### Archivos a MANTENER:
- ✅ Todos los archivos de seguridad JWT
- ✅ `AuthController.java`
- ✅ `SecurityConfig.java`
- ✅ `CorsConfig.java`
- ✅ `EcommerceApplication.java`

### Archivos NUEVOS a crear:
- ✅ `config/RabbitMQConfig.java`
- ✅ `controller/VentaController.java`
- ✅ `controller/PromocionController.java`
- ✅ `model/Venta.java`
- ✅ `model/Promocion.java`
- ✅ `repository/VentaRepository.java`
- ✅ `repository/PromocionRepository.java`
- ✅ `service/RabbitMQProducerService.java`

---

## 📁 EcommerceCloudNative_r2 (Consumer)

### Archivos a ELIMINAR:

#### Controladores
- ❌ `src/main/java/com/example/ecommerce/controller/InstrumentoController.java`
- ❌ `src/main/java/com/example/ecommerce/controller/AuthController.java`

#### Servicios
- ❌ `src/main/java/com/example/ecommerce/service/InstrumentoService.java`

#### Repositorios
- ❌ `src/main/java/com/example/ecommerce/repository/InstrumentoRepository.java`

#### Modelos
- ❌ `src/main/java/com/example/ecommerce/model/Instrumento.java`

#### Seguridad (NO necesarios en el consumidor)
- ❌ `src/main/java/com/example/ecommerce/security/JwtUtil.java`
- ❌ `src/main/java/com/example/ecommerce/security/JwtAuthenticationFilter.java`
- ❌ `src/main/java/com/example/ecommerce/security/JwtAuthenticationEntryPoint.java`

#### DTOs (NO necesarios en el consumidor)
- ❌ `src/main/java/com/example/ecommerce/dto/LoginRequest.java`
- ❌ `src/main/java/com/example/ecommerce/dto/LoginResponse.java`

#### Configuración
- ❌ `src/main/java/com/example/ecommerce/config/DataInitializer.java`
- ❌ `src/main/java/com/example/ecommerce/config/SecurityPasswordLogger.java`

### Archivos a MANTENER pero MODIFICAR:
- ✅ `SecurityConfig.java` (simplificado, sin JWT)
- ✅ `CorsConfig.java`
- ✅ `EcommerceApplication.java`

### Archivos NUEVOS a crear:
- ✅ `config/RabbitMQConfig.java`
- ✅ `controller/MonitorController.java`
- ✅ `service/SalesConsumerService.java`
- ✅ `service/PromotionsConsumerService.java`

### Archivos a COPIAR de r1:
- ✅ `model/Venta.java`
- ✅ `model/Promocion.java`
- ✅ `repository/VentaRepository.java`

---

## 🛠️ Comandos de Limpieza

### PowerShell - Eliminar archivos en r1:
```powershell
# En EcommerceCloudNative_r1
Remove-Item ".\src\main\java\com\example\ecommerce\controller\InstrumentoController.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\service\InstrumentoService.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\repository\InstrumentoRepository.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\model\Instrumento.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\config\DataInitializer.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\config\SecurityPasswordLogger.java" -Force
```

### PowerShell - Eliminar archivos en r2:
```powershell
# En EcommerceCloudNative_r2
Remove-Item ".\src\main\java\com\example\ecommerce\controller\InstrumentoController.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\controller\AuthController.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\service\InstrumentoService.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\repository\InstrumentoRepository.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\model\Instrumento.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\security\*" -Recurse -Force
Remove-Item ".\src\main\java\com\example\ecommerce\dto\*" -Recurse -Force
Remove-Item ".\src\main\java\com\example\ecommerce\config\DataInitializer.java" -Force
Remove-Item ".\src\main\java\com\example\ecommerce\config\SecurityPasswordLogger.java" -Force
```

---

## 📋 Verificación Final

### Estructura final de r1:
```
src/main/java/com/example/ecommerce/
├── config/
│   ├── CorsConfig.java
│   ├── RabbitMQConfig.java ✨
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── PromocionController.java ✨
│   └── VentaController.java ✨
├── dto/
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── model/
│   ├── Promocion.java ✨
│   └── Venta.java ✨
├── repository/
│   ├── PromocionRepository.java ✨
│   └── VentaRepository.java ✨
├── security/
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtUtil.java
├── service/
│   └── RabbitMQProducerService.java ✨
└── EcommerceApplication.java
```

### Estructura final de r2:
```
src/main/java/com/example/ecommerce/
├── config/
│   ├── CorsConfig.java
│   ├── RabbitMQConfig.java ✨
│   └── SecurityConfig.java (simplificado)
├── controller/
│   └── MonitorController.java ✨
├── model/
│   ├── Promocion.java ✨
│   └── Venta.java ✨
├── repository/
│   └── VentaRepository.java ✨
├── service/
│   ├── PromotionsConsumerService.java ✨
│   └── SalesConsumerService.java ✨
└── EcommerceApplication.java
```

✨ = Archivo nuevo o copiado