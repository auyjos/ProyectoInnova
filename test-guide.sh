#!/bin/bash

# ==================================================
# 🚀 GUÍA COMPLETA PARA PROBAR LA APLICACIÓN
# ==================================================

echo "🎯 Guía de Pruebas - Restaurant Reservation Platform"
echo "=================================================="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080"

echo -e "${BLUE}📋 ENDPOINTS DISPONIBLES:${NC}"
echo "================================"
echo ""
echo -e "${YELLOW}🔐 AUTENTICACIÓN:${NC}"
echo "POST /api/auth/register - Registrar nuevo usuario"
echo "POST /api/auth/login    - Iniciar sesión"
echo "POST /api/auth/refresh  - Renovar token"
echo "POST /api/auth/logout   - Cerrar sesión"
echo ""
echo -e "${YELLOW}🏪 RESTAURANTES:${NC}"
echo "GET    /api/restaurants       - Listar restaurantes (público)"
echo "GET    /api/restaurants/{id}  - Ver restaurante específico"
echo "GET    /api/restaurants/search - Buscar restaurantes"
echo "POST   /api/restaurants       - Crear restaurante (PROPIETARIO)"
echo "PUT    /api/restaurants/{id}  - Actualizar restaurante (PROPIETARIO)"
echo "DELETE /api/restaurants/{id}  - Eliminar restaurante (PROPIETARIO)"
echo ""
echo -e "${YELLOW}📅 RESERVAS:${NC}"
echo "GET    /api/reservations           - Listar reservas (autenticado)"
echo "GET    /api/reservations/{id}     - Ver reserva específica"
echo "POST   /api/reservations          - Crear reserva (autenticado)"
echo "PUT    /api/reservations/{id}     - Actualizar reserva"
echo "PATCH  /api/reservations/{id}/status - Cambiar estado"
echo "DELETE /api/reservations/{id}     - Cancelar reserva"
echo ""

echo -e "${GREEN}🧪 EJEMPLOS DE PRUEBAS:${NC}"
echo "========================"
echo ""

echo -e "${BLUE}1. Verificar que la aplicación está corriendo:${NC}"
echo "curl -s $BASE_URL/actuator/health"
echo ""

echo -e "${BLUE}2. Registrar un nuevo usuario:${NC}"
cat << 'EOF'
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phone": "+1234567890"
  }'
EOF
echo ""

echo -e "${BLUE}3. Iniciar sesión:${NC}"
cat << 'EOF'
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
EOF
echo ""

echo -e "${BLUE}4. Listar restaurantes (público):${NC}"
echo "curl -X GET $BASE_URL/api/restaurants"
echo ""

echo -e "${BLUE}5. Crear un restaurante (requiere JWT de PROPIETARIO):${NC}"
cat << 'EOF'
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Restaurante Test",
    "description": "Un excelente restaurante de prueba",
    "address": "Calle Principal 123",
    "city": "Lima",
    "country": "Perú",
    "phone": "+51987654321",
    "email": "contacto@restaurantetest.com",
    "openingTime": "08:00:00",
    "closingTime": "22:00:00",
    "capacity": 50,
    "priceRange": "MEDIUM"
  }'
EOF
echo ""

echo -e "${BLUE}6. Buscar restaurantes:${NC}"
echo "curl -X GET '$BASE_URL/api/restaurants/search?name=test&city=Lima'"
echo ""

echo -e "${BLUE}7. Crear una reserva (requiere JWT):${NC}"
cat << 'EOF'
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "userId": 1,
    "restaurantId": 1,
    "tableId": 1,
    "reservationDateTime": "2025-09-17T19:00:00",
    "numberOfGuests": 4,
    "specialRequests": "Mesa cerca de la ventana"
  }'
EOF
echo ""

echo -e "${RED}⚠️  IMPORTANTE:${NC}"
echo "================"
echo "• Reemplaza YOUR_JWT_TOKEN con el token recibido del login"
echo "• Los IDs (userId, restaurantId, tableId) deben existir en la DB"
echo "• Para endpoints protegidos necesitas autenticación"
echo "• Los roles son: CUSTOMER, RESTAURANT_OWNER, ADMIN"
echo ""

echo -e "${GREEN}🔧 HERRAMIENTAS RECOMENDADAS:${NC}"
echo "=============================="
echo "• Postman: https://www.postman.com/"
echo "• Insomnia: https://insomnia.rest/"
echo "• HTTPie: https://httpie.io/"
echo "• curl (línea de comandos)"
echo ""

echo -e "${YELLOW}💡 TIPS PARA TESTING:${NC}"
echo "====================="
echo "• Usa variables de entorno para tokens JWT"
echo "• Guarda respuestas en archivos para debugging"
echo "• Verifica códigos de estado HTTP"
echo "• Revisa logs de la aplicación en la consola"
echo ""

echo -e "${GREEN}✅ ¡Aplicación lista para testing!${NC}"
echo "Aplicación corriendo en: $BASE_URL"
echo "Logs en tiempo real: mvn spring-boot:run"