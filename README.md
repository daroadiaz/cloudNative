# PASO 1: Limpiar todo lo anterior
docker-compose down -v

# PASO 2: Construir todas las imágenes
docker-compose build --parallel

# PASO 3: Iniciar SOLO la infraestructura (RabbitMQ + Kafka)
docker-compose up -d rabbitmq zookeeper-1 zookeeper-2 zookeeper-3

# PASO 4: Esperar 30 segundos para que RabbitMQ y Zookeeper estén listos
timeout /t 30

# PASO 5: Configurar RabbitMQ
docker exec ecommerce-rabbitmq rabbitmqctl add_vhost /
docker exec ecommerce-rabbitmq rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare exchange name=ecommerce-exchange type=direct durable=true
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare queue name=sales-queue durable=true
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare queue name=promotions-queue durable=true
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare binding source=ecommerce-exchange destination=sales-queue routing_key=sales.routing.key
docker exec ecommerce-rabbitmq rabbitmqadmin -u admin -p admin123 declare binding source=ecommerce-exchange destination=promotions-queue routing_key=promotions.routing.key

# PASO 6: Iniciar Kafka brokers
docker-compose up -d kafka-1 kafka-2 kafka-3

# PASO 6: Iniciar Kafka brokers (Alternativo)
docker-compose up -d ecommerce-kafka-producer ecommerce-kafka-consumer1 ecommerce-kafka-consumer2

# PASO 7: Esperar 30 segundos para que Kafka esté listo
timeout /t 30

# PASO 8: Iniciar el contenedor de inicialización de Kafka
docker-compose up kafka-init

# PASO 9: Iniciar TODOS los microservicios
docker-compose up -d

# PASO 10: Verificar que todo esté corriendo
docker-compose ps

# PASO 11: Ver logs (opcional)
docker-compose logs --tail=50