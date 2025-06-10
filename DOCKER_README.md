# Dockerización de Ecommerce Cloud Native

## Requisitos previos
- Docker Desktop instalado
- Docker Compose instalado
- Wallet de Oracle en la carpeta correcta

## Estructura del proyecto
```
new tarea/cloudNative/
├── EcommerceCloudNative/          # Backend Spring Boot
│   ├── Wallet_EcommerceCloudNative/  # Wallet de Oracle (en la raíz del backend)
│   ├── src/
│   ├── target/
│   ├── Dockerfile
│   ├── Dockerfile.dev
│   ├── .dockerignore
│   ├── pom.xml
│   └── mvnw
├── ecommerce-frontend/            # Frontend Angular
│   ├── src/
│   ├── node_modules/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── .dockerignore
├── docker-compose.yml             # Orquestación producción
├── docker-compose.dev.yml         # Orquestación desarrollo
├── Makefile                       # Scripts de automatización
└── DOCKER_README.md              # Este archivo
```

## Comandos rápidos

### Producción
```bash
# Construir imágenes
docker-compose build

# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose down
```

### Desarrollo (con hot reload)
```bash
# Iniciar en modo desarrollo
docker-compose -f docker-compose.dev.yml up

# En otra terminal, ver logs
docker-compose -f docker-compose.dev.yml logs -f
```

### Usando Makefile
```bash
# Producción
make build
make up
make logs
make down

# Desarrollo
make dev-up
make dev-down

# Limpiar todo
make clean
```

## URLs de acceso

### Producción
- Frontend: http://localhost
- Backend API: http://localhost:8080/api

### Desarrollo
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api

## Credenciales
- Usuario: Admin
- Contraseña: CloudNative_123

## Solución de problemas

### Error de conexión a Oracle
1. Verificar que el Wallet esté en `EcommerceCloudNative/Wallet_EcommerceCloudNative/`
2. Verificar que el TNS_ADMIN apunte correctamente en el Dockerfile

### Error de CORS
1. Verificar que el backend esté ejecutándose
2. Revisar los logs del backend: `docker logs ecommerce-backend`

### Frontend no se conecta al backend
1. Verificar que ambos contenedores estén en la misma red
2. Revisar la configuración de nginx.conf

## Construcción de imágenes individuales

```bash
# Backend
cd EcommerceCloudNative
docker build -t ecommerce-backend .

# Frontend
cd ecommerce-frontend
docker build -t ecommerce-frontend .
```

## Variables de entorno importantes

### Backend
- `SPRING_PROFILES_ACTIVE=docker`
- `TNS_ADMIN=/app/Wallet_EcommerceCloudNative`

### Frontend
- Las URLs de API se manejan mediante nginx proxy

## Monitoreo

```bash
# Ver uso de recursos
docker stats

# Ver contenedores en ejecución
docker ps

# Inspeccionar un contenedor
docker inspect ecommerce-backend
```