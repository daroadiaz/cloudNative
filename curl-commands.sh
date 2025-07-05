#!/bin/bash

# Colección de comandos cURL para el sistema de Ecommerce con RabbitMQ

echo "=== Comandos cURL para Ecommerce con RabbitMQ ==="
echo ""

# Variables
PRODUCER_URL="http://localhost:8081"
CONSUMER_URL="http://localhost:8082"

# 1. PRODUCER - Login para obtener JWT
echo "1. LOGIN en Producer (r1):"
echo "curl -X POST $PRODUCER_URL/api/auth/login \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d '{\"username\": \"Admin\", \"password\": \"CloudNative_123\"}'"
echo ""

# Ejecutar y guardar token
TOKEN_RESPONSE=$(curl -s -X POST $PRODUCER_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "Admin", "password": "CloudNative_123"}')

TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"token":"[^"]*' | sed 's/"token":"//')
echo "Token obtenido: $TOKEN"
echo ""

# 2. PRODUCER - Procesar Venta
echo "2. PROCESAR VENTA (Productor 1):"
curl -X POST $PRODUCER_URL/api/ventas/procesar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "productoId": 1,
    "productoNombre": "Guitarra Fender Stratocaster",
    "cantidad": 2,
    "precioUnitario": 1500.00,
    "clienteNombre": "Juan Pérez",
    "clienteEmail": "juan.perez@email.com"
  }'
echo -e "\n"

# 3. PRODUCER - Actualizar Promoción
echo "3. ACTUALIZAR PROMOCIÓN (Productor 2):"
curl -X POST $PRODUCER_URL/api/promociones/actualizar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "codigo": "BLACKFRIDAY2024",
    "descripcion": "50% de descuento en guitarras eléctricas",
    "tipoDescuento": "PORCENTAJE",
    "valorDescuento": 50.0,
    "categoria": "GUITARRAS",
    "fechaInicio": "2024-11-24T00:00:00",
    "fechaFin": "2024-11-30T23:59:59",
    "stockLimite": 100
  }'
echo -e "\n"

# 4. PRODUCER - Enviar múltiples promociones
echo "4. ENVIAR PROMOCIONES EN LOTE:"
curl -X POST $PRODUCER_URL/api/promociones/batch \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '[
    {
      "codigo": "CYBERMONDAY2024",
      "descripcion": "30% en todos los instrumentos",
      "tipoDescuento": "PORCENTAJE",
      "valorDescuento": 30.0,
      "categoria": "TODOS",
      "fechaInicio": "2024-12-01T00:00:00",
      "fechaFin": "2024-12-01T23:59:59"
    },
    {
      "codigo": "NAVIDAD2024",
      "descripcion": "Descuento de $200 en compras mayores a $1000",
      "tipoDescuento": "MONTO_FIJO",
      "valorDescuento": 200.0,
      "categoria": "TODOS",
      "fechaInicio": "2024-12-15T00:00:00",
      "fechaFin": "2024-12-25T23:59:59"
    }
  ]'
echo -e "\n"

# 5. CONSUMER - Verificar Health
echo "5. VERIFICAR HEALTH DEL CONSUMIDOR:"
curl -X GET $CONSUMER_URL/api/monitor/health
echo -e "\n"

# 6. CONSUMER - Listar archivos JSON generados
echo "6. LISTAR ARCHIVOS JSON GENERADOS:"
curl -X GET $CONSUMER_URL/api/monitor/files
echo -e "\n"

# 7. CONSUMER - Ver estadísticas
echo "7. VER ESTADÍSTICAS DEL CONSUMIDOR:"
curl -X GET $CONSUMER_URL/api/monitor/stats
echo -e "\n"

# 8. PRODUCER - Health checks
echo "8. HEALTH CHECKS DE PRODUCTORES:"
echo "Ventas:"
curl -X GET $PRODUCER_URL/api/ventas/health -H "Authorization: Bearer $TOKEN"
echo -e "\n"
echo "Promociones:"
curl -X GET $PRODUCER_URL/api/promociones/health -H "Authorization: Bearer $TOKEN"
echo -e "\n"

# 9. Múltiples ventas para pruebas de carga
echo "9. ENVIAR MÚLTIPLES VENTAS (Prueba de carga):"
for i in {1..5}
do
  echo "Enviando venta $i..."
  curl -s -X POST $PRODUCER_URL/api/ventas/procesar \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d "{
      \"productoId\": $i,
      \"productoNombre\": \"Producto Test $i\",
      \"cantidad\": $((RANDOM % 5 + 1)),
      \"precioUnitario\": $((RANDOM % 1000 + 100)).00,
      \"clienteNombre\": \"Cliente Test $i\",
      \"clienteEmail\": \"cliente$i@test.com\"
    }" > /dev/null
done
echo "✓ 5 ventas enviadas"
echo ""

echo "=== Fin de pruebas ==="