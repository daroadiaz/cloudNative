# 🔧 Guía de Solución de Problemas

## Problemas Comunes y Soluciones

### 1. Error: Puerto en uso

**Síntoma:**
```
Error starting userland proxy: listen tcp4 0.0.0.0:8081: bind: address already in use
```

**Solución:**
```powershell
# Verificar qué proceso usa el puerto
netstat -ano | findstr :8081

# Matar el proceso (reemplazar PID con el número obtenido)
taskkill /PID <PID> /F

# O cambiar el puerto en docker-compose.yml
```

### 2. Error: RabbitMQ no se conecta

**Síntoma:**
```
Failed to connect to RabbitMQ: Connection refused
```

**Solución:**
```powershell
# Verificar que RabbitMQ esté corriendo
docker ps | findstr rabbitmq

# Verificar logs de RabbitMQ
docker-compose logs rabbitmq

# Reiniciar RabbitMQ
docker-compose restart rabbitmq

# Verificar conectividad
telnet localhost 5672
```

### 3. Error: Oracle Wallet no encontrado

**Síntoma:**
```
ORA-12263: TNS:Failed to access wallet
```

**Solución:**
1. Verificar ubicación del wallet:
   ```powershell
   # En cada proyecto
   dir Wallet_EcommerceCloudNative
   ```

2. Verificar permisos:
   ```powershell
   icacls Wallet_EcommerceCloudNative
   ```

3. Verificar ruta en `application.properties`:
   ```properties
   # Debe ser:
   spring.datasource.url=jdbc:oracle:thin:@ecommercecloudnative_high?TNS_ADMIN=./Wallet_EcommerceCloudNative
   ```

### 4. Error: Maven build failure

**Síntoma:**
```
[ERROR] Failed to execute goal...
```

**Solución:**
```powershell
# Limpiar y reconstruir
mvn clean
mvn install -DskipTests

# Si hay problemas de dependencias
mvn dependency:purge-local-repository
mvn clean install
```

### 5. Error: No se generan archivos JSON

**Síntoma:**
Los archivos JSON de promociones no se crean.

**Solución:**
1. Verificar que el directorio existe:
   ```powershell
   mkdir -p output/promociones
   ```

2. Verificar permisos del directorio
3. Revisar logs del consumidor:
   ```powershell
   docker-compose logs -f ecommerce-consumer
   ```

### 6. Error: JWT Token inválido

**Síntoma:**
```
401 Unauthorized - Token JWT no válido o ausente
```

**Solución:**
1. Verificar que el token se esté enviando correctamente
2. Verificar que el secret sea el mismo en ambos archivos properties
3. Generar un nuevo token con login

### 7. Error: Contenedor se reinicia constantemente

**Síntoma:**
El contenedor Docker se reinicia en loop.

**Solución:**
```powershell
# Ver logs del contenedor
docker-compose logs ecommerce-producer

# Verificar salud
docker-compose ps

# Reconstruir imagen
docker-compose build --no-cache ecommerce-producer
```

### 8. Error: No se pueden ver las colas en RabbitMQ

**Síntoma:**
Las colas no aparecen en la interfaz de gestión.

**Solución:**
1. Ejecutar el script de configuración:
   ```powershell
   .\setup-rabbitmq.ps1
   ```

2. Verificar manualmente:
   ```bash
   docker exec ecommerce-rabbitmq rabbitmqctl list_queues
   docker exec ecommerce-rabbitmq rabbitmqctl list_exchanges
   ```

### 9. Error: Timeout al conectar con Oracle

**Síntoma:**
```
Connection timeout to Oracle database
```

**Solución:**
1. Verificar conectividad de red
2. Verificar credenciales en properties
3. Aumentar timeout:
   ```properties
   spring.datasource.hikari.connection-timeout=60000
   ```

### 10. Error: OutOfMemoryError

**Síntoma:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Solución:**
En el Dockerfile, modificar ENTRYPOINT:
```dockerfile
ENTRYPOINT ["java", "-Xmx512m", "-jar", "/app/app.jar"]
```

## Comandos Útiles de Diagnóstico

### Ver todos los logs
```powershell
docker-compose logs
```

### Ver logs en tiempo real
```powershell
docker-compose logs -f --tail 100
```

### Verificar estado de contenedores
```powershell
docker-compose ps
docker stats
```

### Acceder a un contenedor
```bash
docker exec -it ecommerce-rabbitmq bash
docker exec -it ecommerce-producer sh
docker exec -it ecommerce-consumer sh
```

### Limpiar todo y empezar de nuevo
```powershell
docker-compose down -v
docker system prune -a
.\start-system.ps1
```

### Verificar red Docker
```powershell
docker network ls
docker network inspect cloudnative_ecommerce-network
```

## Logs importantes a revisar

1. **Producer startup:**
   - Buscar: "Started EcommerceApplication"
   - Buscar: "Connected to RabbitMQ"

2. **Consumer startup:**
   - Buscar: "Started listening on queue"
   - Buscar: "Consumer ready"

3. **RabbitMQ:**
   - Buscar: "Server startup complete"
   - Buscar: "accepting AMQP connection"

## Contacto y Soporte

Si encuentras otros problemas:
1. Revisa los logs detalladamente
2. Verifica la configuración paso a paso
3. Asegúrate de que todos los servicios estén corriendo
4. Verifica la conectividad entre servicios