#!/bin/bash

# Script para inicializar datos de prueba en la aplicación
# Ejecutar después de que la aplicación esté corriendo

BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api"

echo "🚀 Inicializando datos de prueba para Restaurant Reservation Platform"
echo "============================================================"

# Función para hacer requests HTTP con validación
make_request() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    local token=$5
    
    echo -e "\n📋 $description"
    echo "🌐 $method $url"
    
    if [ -n "$token" ]; then
        if [ -n "$data" ]; then
            response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" \
                -X "$method" \
                -H "Content-Type: application/json" \
                -H "Authorization: Bearer $token" \
                -d "$data" \
                "$url")
        else
            response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" \
                -X "$method" \
                -H "Authorization: Bearer $token" \
                "$url")
        fi
    else
        if [ -n "$data" ]; then
            response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" \
                -X "$method" \
                -H "Content-Type: application/json" \
                -d "$data" \
                "$url")
        else
            response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" \
                -X "$method" \
                "$url")
        fi
    fi
    
    status_code=$(echo "$response" | grep -o "HTTP_STATUS:[0-9]*" | cut -d: -f2)
    response_body=$(echo "$response" | sed '/HTTP_STATUS:/d')
    
    if [ "$status_code" -ge 200 ] && [ "$status_code" -lt 300 ]; then
        echo "✅ Éxito (HTTP $status_code)"
        echo "$response_body" | jq '.' 2>/dev/null || echo "$response_body"
    else
        echo "❌ Error (HTTP $status_code)"
        echo "$response_body" | jq '.' 2>/dev/null || echo "$response_body"
    fi
    
    echo "$response_body"
}

# 1. Verificar que la aplicación esté corriendo
echo -e "\n🔍 Verificando que la aplicación esté corriendo..."
health_check=$(curl -s "$BASE_URL/actuator/health" 2>/dev/null)
if [[ $? -ne 0 ]]; then
    echo "❌ Error: La aplicación no está corriendo en $BASE_URL"
    echo "💡 Asegúrate de ejecutar: mvn spring-boot:run"
    exit 1
fi
echo "✅ Aplicación corriendo correctamente"

# 2. Registrar usuarios de prueba
echo -e "\n👥 Registrando usuarios de prueba..."

# Usuario regular
customer_data='{
  "username": "customer1",
  "email": "customer1@test.com",
  "password": "password123",
  "firstName": "Juan",
  "lastName": "Pérez",
  "phone": "+51987654321"
}'

make_request "POST" "$API_BASE/auth/register" "$customer_data" "Registrando cliente"

# Usuario propietario de restaurante
owner_data='{
  "username": "owner1",
  "email": "owner1@test.com",
  "password": "password123",
  "firstName": "María",
  "lastName": "García",
  "phone": "+51987654322"
}'

make_request "POST" "$API_BASE/auth/register" "$owner_data" "Registrando propietario"

# 3. Login del propietario para crear restaurantes
echo -e "\n🔐 Autenticando propietario..."
login_data='{
  "username": "owner1",
  "password": "password123"
}'

login_response=$(make_request "POST" "$API_BASE/auth/login" "$login_data" "Login propietario")
owner_token=$(echo "$login_response" | jq -r '.accessToken' 2>/dev/null)

if [ "$owner_token" = "null" ] || [ -z "$owner_token" ]; then
    echo "❌ Error: No se pudo obtener token del propietario"
    exit 1
fi

echo "✅ Token obtenido: ${owner_token:0:50}..."

# 4. Crear restaurantes de prueba
echo -e "\n🏪 Creando restaurantes de prueba..."

restaurant1_data='{
  "name": "La Pizzería Italiana",
  "description": "Auténtica cocina italiana con pizzas artesanales y pastas caseras",
  "address": "Av. Larco 1234",
  "city": "Lima",
  "country": "Perú",
  "phone": "+51987654323",
  "email": "contacto@pizzeriaitaliana.com",
  "openingTime": "12:00:00",
  "closingTime": "23:00:00",
  "capacity": 40,
  "priceRange": "MEDIUM"
}'

make_request "POST" "$API_BASE/restaurants" "$restaurant1_data" "Creando La Pizzería Italiana" "$owner_token"

restaurant2_data='{
  "name": "Sushi Zen",
  "description": "Experiencia gastronómica japonesa con sushi fresco y ambiente tradicional",
  "address": "Av. El Sol 567",
  "city": "Lima",
  "country": "Perú",
  "phone": "+51987654324",
  "email": "info@sushizen.com",
  "openingTime": "18:00:00",
  "closingTime": "01:00:00",
  "capacity": 30,
  "priceRange": "HIGH"
}'

make_request "POST" "$API_BASE/restaurants" "$restaurant2_data" "Creando Sushi Zen" "$owner_token"

restaurant3_data='{
  "name": "Café Central",
  "description": "Café acogedor con desayunos, almuerzos ligeros y postres caseros",
  "address": "Jr. Unión 890",
  "city": "Lima",
  "country": "Perú",
  "phone": "+51987654325",
  "email": "hola@cafecentral.com",
  "openingTime": "07:00:00",
  "closingTime": "20:00:00",
  "capacity": 25,
  "priceRange": "LOW"
}'

make_request "POST" "$API_BASE/restaurants" "$restaurant3_data" "Creando Café Central" "$owner_token"

# 5. Login del cliente para hacer reservas
echo -e "\n🔐 Autenticando cliente..."
customer_login_data='{
  "username": "customer1",
  "password": "password123"
}'

customer_login_response=$(make_request "POST" "$API_BASE/auth/login" "$customer_login_data" "Login cliente")
customer_token=$(echo "$customer_login_response" | jq -r '.accessToken' 2>/dev/null)

if [ "$customer_token" = "null" ] || [ -z "$customer_token" ]; then
    echo "❌ Error: No se pudo obtener token del cliente"
    exit 1
fi

echo "✅ Token cliente obtenido: ${customer_token:0:50}..."

# 6. Listar restaurantes disponibles
echo -e "\n📋 Consultando restaurantes disponibles..."
make_request "GET" "$API_BASE/restaurants?page=0&size=10" "" "Lista de restaurantes"

# 7. Buscar restaurantes
echo -e "\n🔍 Buscando restaurantes..."
make_request "GET" "$API_BASE/restaurants/search?city=Lima" "" "Búsqueda por ciudad"

# 8. Crear reservas de prueba
echo -e "\n📅 Creando reservas de prueba..."

# Reserva para mañana en La Pizzería (asumiendo ID 1)
tomorrow=$(date -d "+1 day" +%Y-%m-%d)
reservation1_data='{
  "userId": 1,
  "restaurantId": 1,
  "tableId": 1,
  "reservationDateTime": "'$tomorrow'T19:00:00",
  "numberOfGuests": 4,
  "specialRequests": "Mesa cerca de la ventana, celebración de cumpleaños"
}'

make_request "POST" "$API_BASE/reservations" "$reservation1_data" "Reserva en La Pizzería" "$customer_token"

# Reserva para pasado mañana en Sushi Zen (asumiendo ID 2)
day_after_tomorrow=$(date -d "+2 days" +%Y-%m-%d)
reservation2_data='{
  "userId": 1,
  "restaurantId": 2,
  "tableId": 2,
  "reservationDateTime": "'$day_after_tomorrow'T20:30:00",
  "numberOfGuests": 2,
  "specialRequests": "Preferencia por mesa en el área de sushi bar"
}'

make_request "POST" "$API_BASE/reservations" "$reservation2_data" "Reserva en Sushi Zen" "$customer_token"

# 9. Consultar reservas del cliente
echo -e "\n📋 Consultando reservas del cliente..."
make_request "GET" "$API_BASE/reservations?page=0&size=10" "" "Mis reservas" "$customer_token"

# 10. Confirmar una reserva (asumiendo ID 1)
echo -e "\n✅ Confirmando reserva..."
make_request "PATCH" "$API_BASE/reservations/1/confirm" "" "Confirmar reserva #1" "$customer_token"

echo -e "\n🎉 ¡Inicialización de datos completada!"
echo "============================================================"
echo "📊 Resumen de datos creados:"
echo "  👥 2 usuarios registrados (customer1, owner1)"
echo "  🏪 3 restaurantes creados"
echo "  📅 2 reservas de prueba"
echo ""
echo "🔗 Endpoints disponibles:"
echo "  🌐 API Base: $API_BASE"
echo "  📚 Health: $BASE_URL/actuator/health"
echo ""
echo "💡 Próximos pasos:"
echo "  1. Importar Restaurant_API_Collection.postman_collection.json en Postman"
echo "  2. Usar tokens obtenidos para probar endpoints protegidos"
echo "  3. Ejecutar ./test-guide.sh para más ejemplos de pruebas"