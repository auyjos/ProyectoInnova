# Usar OpenJDK 17 como base
FROM openjdk:17-jdk-slim

# Instalar Maven y herramientas necesarias
RUN apt-get update && apt-get install -y \
    curl \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos Maven
COPY pom.xml .

# Descargar dependencias (para cachear)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/restaurant-reservation-platform-1.0.0-SNAPSHOT.jar"]