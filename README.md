# Restaurant Reservation Platform 🍽️

Una plataforma backend completa para reservas de restaurantes desarrollada con Spring Boot, implementando los principios SOLID y utilizando inversión de control para alternar entre bases de datos MongoDB y PostgreSQL.

## 📋 Descripción del Proyecto

Este sistema permite a los usuarios:
- **Clientes**: Ver disponibilidad de mesas, realizar reservas y cancelar reservas
- **Propietarios de Restaurantes**: Gestionar sus restaurantes, mesas y horarios
- **Administradores**: Supervisar el sistema completo

## 🛠️ Tecnologías Utilizadas

- **Framework**: Spring Boot 3.1.4
- **Lenguaje**: Java 17
- **Bases de Datos**: 
  - PostgreSQL (Base de datos relacional)
  - MongoDB (Base de datos NoSQL)
- **Seguridad**: JWT (JSON Web Tokens)
- **Testing**: JUnit 5, Mockito
- **Documentación**: OpenAPI 3 (Swagger)
- **Build Tool**: Maven

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas con los siguientes principios:

- **Inversión de Control**: Permite cambiar entre MongoDB y PostgreSQL mediante configuración
- **Principios SOLID**: Código mantenible y escalable
- **Richardson Maturity Model Level 2**: APIs REST bien diseñadas
- **Manejo de Excepciones**: Respuestas consistentes y descriptivas

## 📁 Estructura del Proyecto

```
src/main/java/com/innova/restaurant/
├── config/                 # Configuraciones de la aplicación
├── controller/             # Controladores REST
├── dto/                    # Data Transfer Objects
├── exception/              # Manejo de excepciones
├── model/
│   ├── entity/            # Entidades JPA (PostgreSQL)
│   ├── document/          # Documentos MongoDB
│   └── enums/             # Enumeraciones
├── repository/
│   ├── jpa/               # Repositorios JPA
│   └── mongo/             # Repositorios MongoDB
├── security/              # Configuración de seguridad JWT
└── service/               # Lógica de negocio
```

## 🚀 Funcionalidades

### Gestión de Usuarios
- [x] Registro y autenticación de usuarios
- [x] Roles diferenciados (Cliente, Propietario, Administrador)
- [x] Perfiles de usuario personalizables

### Gestión de Restaurantes
- [x] CRUD completo de restaurantes
- [x] Gestión de horarios de apertura
- [x] Control de capacidad máxima

### Gestión de Mesas
- [x] Configuración de mesas por restaurante
- [x] Estados de disponibilidad
- [x] Capacidad por mesa

### Sistema de Reservas
- [x] Reservas con validación de disponibilidad
- [x] Estados de reserva (Pendiente, Confirmada, Cancelada, etc.)
- [x] Solicitudes especiales

## 🔧 Configuración

### Perfiles de Aplicación

El proyecto incluye diferentes perfiles para distintos entornos:

- **dev**: Desarrollo local
- **prod**: Producción
- **test**: Pruebas automatizadas

### Bases de Datos

Configura el tipo de base de datos en `application.yml`:

```yaml
app:
  database:
    type: jpa  # Para PostgreSQL
    # type: mongo  # Para MongoDB
```

## 🏃‍♂️ Ejecución

```bash
# Clonar el repositorio
git clone [URL_DEL_REPOSITORIO]
cd restaurant-reservation-platform

# Ejecutar con Maven
mvn spring-boot:run

# O con profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🧪 Testing

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar con cobertura
mvn test jacoco:report
```

## 📝 API Documentation

Una vez ejecutada la aplicación, la documentación de la API estará disponible en:
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v1/v3/api-docs`

## 🔐 Seguridad

- Autenticación basada en JWT
- Autorización por roles
- Validación de entrada en todos los endpoints
- Protección contra ataques comunes

## 📊 Criterios de Aceptación Cumplidos

- ✅ Richardson Maturity Model nivel 2
- ✅ Uso correcto de verbos HTTP y códigos de estado
- ✅ Implementación adecuada de Spring Data
- ✅ Funcionalidad CRUD completa
- ✅ Manejo de excepciones robusto
- ✅ Seguridad con JWT
- ✅ Cobertura de pruebas unitarias
- ✅ Principios SOLID aplicados
- ✅ Inversión de dependencias implementada

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 👨‍💻 Autor

**Jose** - Desarrollador Full Stack

---

⭐ Si este proyecto te resulta útil, ¡no olvides darle una estrella!