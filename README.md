# 🍽️ Restaurant Reservation Platform

Una plataforma moderna de reservas de restaurantes construida con **Spring Boot 3**, **JWT Security**, y arquitectura híbrida **PostgreSQL + MongoDB**. Implementa patrones enterprise y APIs REST Richardson Level 2.

## 🎯 Características Principales

### 🔒 Seguridad Enterprise
- **JWT Authentication & Authorization** con roles (CUSTOMER, RESTAURANT_OWNER, ADMIN)
- **Spring Security 6** con filtros personalizados
- **Refresh Token** support para sesiones extendidas
- **CORS** configurado para desarrollo frontend
- **Password Encryption** con BCrypt

### 🏗️ Arquitectura Robusta
- **Richardson Level 2 REST APIs** con HTTP verbs y status codes apropiados
- **Service Layer Pattern** con inyección de dependencias
- **Global Exception Handling** con respuestas JSON estandarizadas
- **Bean Validation** con anotaciones JSR-303
- **Hybrid Database**: PostgreSQL (relacional) + MongoDB (documentos)

### 📊 Gestión de Datos
- **Spring Data JPA** con consultas automáticas (sin @Query)
- **Automatic Query Methods** basados en nombres de métodos
- **Pagination & Sorting** en endpoints de listado
- **Entity Relationships** con manejo optimizado de lazy loading

## 🛠️ Stack Tecnológico

| Componente | Tecnología | Versión |
|------------|------------|---------|
| **Framework** | Spring Boot | 3.1.4 |
| **Java** | OpenJDK | 17+ |
| **Security** | Spring Security | 6.x |
| **Database** | PostgreSQL | 15+ |
| **NoSQL** | MongoDB | 6+ |
| **ORM** | Spring Data JPA | 3.x |
| **ODM** | Spring Data MongoDB | 4.x |
| **Build** | Maven | 3.8+ |
| **JWT** | JJWT | 0.11.5 |

## 🚀 Funcionalidades

### Gestión de Usuarios
- [x] Registro y autenticación de usuarios
- [x] Roles diferenciados (Cliente, Propietario, Administrador)
- [x] Perfiles de usuario personalizables

### Gestión de Restaurantes
- [x] CRUD completo de restaurantes
## 📋 Requisitos Previos

```bash
# Java 17+
java -version

# Maven 3.8+
mvn -version

# PostgreSQL 15+ (corriendo en puerto 5432)
psql --version

# MongoDB 6+ (opcional - para funcionalidad completa)
mongod --version
```

## 🚀 Instalación y Configuración

### 1. Clonar y Compilar

```bash
# Navegar al directorio del proyecto
cd /home/jose/Documents/Personal/ProyectoInnova

# Compilar el proyecto
mvn clean compile

# Verificar dependencias
mvn dependency:tree
```

### 2. Configuración de Base de Datos

#### PostgreSQL Setup
```sql
-- Crear base de datos
CREATE DATABASE restaurant_reservation_db;

-- Crear usuario (opcional)
CREATE USER restaurant_user WITH PASSWORD 'restaurant_pass';
GRANT ALL PRIVILEGES ON DATABASE restaurant_reservation_db TO restaurant_user;
```

#### MongoDB Setup (Opcional)
```bash
# Iniciar MongoDB
sudo systemctl start mongod

# Crear base de datos (se crea automáticamente)
# Las colecciones se crean al insertar el primer documento
```

### 3. Configuración de Aplicación

El archivo `application.properties` está configurado para:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/restaurant_reservation_db
spring.datasource.username=postgres
spring.datasource.password=admin

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# MongoDB (opcional)
spring.data.mongodb.uri=mongodb://localhost:27017/restaurant_reservation_mongo

# JWT
jwt.secret=mySecretKey
jwt.expiration=86400000
jwt.refresh-expiration=604800000

# Server
server.port=8080
```

## � Ejecución

### Opción 1: Maven Spring Boot Plugin
```bash
mvn spring-boot:run
```

### Opción 2: JAR Compilado
```bash
mvn package -DskipTests
java -jar target/restaurant-reservation-platform-0.0.1-SNAPSHOT.jar
```

### Verificación de Startup
```bash
# Health Check
curl http://localhost:8080/actuator/health

# Respuesta esperada:
# {"status":"UP"}
```

## 🧪 Testing Automatizado

### 1. Script de Inicialización de Datos
```bash
# Ejecutar después de iniciar la aplicación
./init-test-data.sh
```

Este script:
- ✅ Registra usuarios de prueba (customer1, owner1)
- ✅ Crea 3 restaurantes de ejemplo
- ✅ Genera reservas de prueba
- ✅ Demuestra flujo completo de autenticación

### 2. Guía de Testing Manual
```bash
# Ejemplos con curl para todos los endpoints
./test-guide.sh
```

### 3. Colección de Postman
Importar `Restaurant_API_Collection.postman_collection.json` para testing completo con:
- 🔐 Endpoints de autenticación con auto-captura de JWT
- 🏪 CRUD de restaurantes
- 📅 Gestión de reservas
- 🏥 Health checks

## 📖 API Documentation

### 🔐 Authentication Endpoints

| Endpoint | Method | Descripción |
|----------|--------|-------------|
| `/api/auth/register` | POST | Registro de nuevos usuarios |
| `/api/auth/login` | POST | Autenticación con JWT |
| `/api/auth/refresh` | POST | Renovar token de acceso |
| `/api/auth/logout` | POST | Cerrar sesión |

### 🏪 Restaurant Endpoints

| Endpoint | Method | Auth | Descripción |
|----------|--------|------|-------------|
| `/api/restaurants` | GET | Public | Listar restaurantes (paginado) |
| `/api/restaurants/{id}` | GET | Public | Obtener restaurante por ID |
| `/api/restaurants/search` | GET | Public | Buscar restaurantes |
| `/api/restaurants` | POST | Owner+ | Crear restaurante |
| `/api/restaurants/{id}` | PUT | Owner+ | Actualizar restaurante |

### � Reservation Endpoints

| Endpoint | Method | Auth | Descripción |
|----------|--------|------|-------------|
| `/api/reservations` | GET | User | Mis reservas (paginado) |
| `/api/reservations/{id}` | GET | User | Obtener reserva por ID |
| `/api/reservations` | POST | User | Crear nueva reserva |
| `/api/reservations/{id}` | PUT | User | Actualizar reserva |
| `/api/reservations/{id}/status` | PATCH | User | Cambiar estado |
| `/api/reservations/{id}/confirm` | PATCH | User | Confirmar reserva |
| `/api/reservations/{id}/checkin` | PATCH | User | Check-in |
| `/api/reservations/{id}` | DELETE | User | Cancelar reserva |

## 🎯 Roadmap y Mejoras Futuras

### Fase 1: Funcionalidad Core ✅
- [x] Autenticación JWT
- [x] CRUD Restaurantes
- [x] Sistema de Reservas
- [x] Validaciones de Negocio

### Fase 2: Testing y Documentación ✅
- [x] Scripts de Testing
- [x] Colección Postman
- [x] Documentación API
- [x] Inicialización de Datos

### Fase 3: Mejoras Avanzadas 🔄
- [ ] Unit Tests (JUnit + Mockito)
- [ ] Integration Tests
- [ ] OpenAPI/Swagger Documentation
- [ ] Rate Limiting
- [ ] Caching con Redis
- [ ] Event-Driven Architecture
- [ ] Notification System
- [ ] Payment Integration

### Fase 4: Production Ready 🔄
- [ ] Docker Containerization
- [ ] CI/CD Pipeline
- [ ] Monitoring con Prometheus
- [ ] Logging Centralizado
- [ ] Environment Configurations
- [ ] Performance Testing

## � Soporte

- **Documentation**: [README.md](README.md)
- **Testing Guide**: `./test-guide.sh`
- **Data Initialization**: `./init-test-data.sh`
- **Postman Collection**: `Restaurant_API_Collection.postman_collection.json`

---

**Desarrollado con ❤️ usando Spring Boot 3 y mejores prácticas enterprise**