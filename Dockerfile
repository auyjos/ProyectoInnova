# Approach súper simple: Solo ejecutar como lo hacemos aquí
FROM maven:latest

WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Ejecutar exactamente como lo hacemos aquí: mvn spring-boot:run pero en Railway
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=railway"]