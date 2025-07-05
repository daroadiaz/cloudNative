Para Windows (PowerShell):



# 1. Limpiar todo y empezar de cero
docker-compose down -v

# 2. Construir las imágenes
docker-compose build --parallel

# 3. Iniciar solo RabbitMQ primero
docker-compose up -d

# 4. Esperar que RabbitMQ esté completamente listo (30 segundos)
Start-Sleep -Seconds 30

# 5. Configurar RabbitMQ completamente
# Crear vhost (aunque / ya existe por defecto)
docker exec ecommerce-rabbitmq rabbitmqctl add_vhost /
docker exec ecommerce-rabbitmq rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"

# Crear exchange
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare exchange name=ecommerce-exchange type=direct durable=true

# Crear colas
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare queue name=sales-queue durable=true
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare queue name=promotions-queue durable=true

# Crear bindings
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare binding source=ecommerce-exchange destination=sales-queue routing_key=sales.routing.key
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare binding source=ecommerce-exchange destination=promotions-queue routing_key=promotions.routing.key

# 6. Verificar la configuración
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 list exchanges name type
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 list queues name messages
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 list bindings source destination routing_key

# 7. Ahora iniciar el resto de servicios
docker-compose up -d

# 8. Esperar 30 segundos para que los servicios se conecten
Start-Sleep -Seconds 30

# 9. Verificar el estado de todos los servicios
docker-compose ps

# 10. Ver los logs para verificar que no hay errores
docker-compose logs --tail=50








Para Amazon Linux:




# 1. Instalar Docker y Docker Compose si no están instalados
sudo yum update -y
sudo yum install -y docker git
sudo service docker start
sudo usermod -a -G docker ec2-user

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 2. Clonar o copiar tu proyecto
git clone [tu-repositorio] cloudNative
cd cloudNative

# 3. Limpiar todo y empezar de cero
docker-compose down -v

# 4. Construir las imágenes
docker-compose build --parallel

# 5. Iniciar solo RabbitMQ primero
docker-compose up -d

# 6. Esperar que RabbitMQ esté listo
sleep 30

# 7. Configurar RabbitMQ completamente
# Crear vhost
docker exec ecommerce-rabbitmq rabbitmqctl add_vhost /
docker exec ecommerce-rabbitmq rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"

# Crear exchange
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare exchange name=ecommerce-exchange type=direct durable=true

# Crear colas
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare queue name=sales-queue durable=true
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare queue name=promotions-queue durable=true

# Crear bindings
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare binding source=ecommerce-exchange destination=sales-queue routing_key=sales.routing.key
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare binding source=ecommerce-exchange destination=promotions-queue routing_key=promotions.routing.key

# 8. Verificar la configuración
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 list exchanges name type
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 list queues name messages
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 list bindings source destination routing_key

# 9. Iniciar el resto de servicios
docker-compose up -d

# 10. Esperar que los servicios se conecten
sleep 30

# 11. Verificar el estado
docker-compose ps

# 12. Ver logs
docker-compose logs --tail=50