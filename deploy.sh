#!/bin/bash

# Script de deployment simplificado para Restaurant Reservation Platform
# Este script automatiza el proceso de compilación, testing y deployment

set -e  # Salir si cualquier comando falla

echo "🚀 Restaurant Reservation Platform - Deploy Script"
echo "================================================="

# Variables de configuración
PROJECT_DIR="/home/jose/Documents/Personal/ProyectoInnova"
APP_NAME="restaurant-reservation-platform"
APP_VERSION="0.0.1-SNAPSHOT"
PORT=8080

# Función para logging con timestamp
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

# Función para verificar prerrequisitos
check_prerequisites() {
    log "🔍 Verificando prerrequisitos..."
    
    # Verificar Java
    if ! command -v java &> /dev/null; then
        echo "❌ Java no está instalado. Se requiere Java 17+"
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
    if [ "$java_version" -lt 17 ]; then
        echo "❌ Se requiere Java 17 o superior. Versión actual: $java_version"
        exit 1
    fi
    log "✅ Java $java_version detectado"
    
    # Verificar Maven
    if ! command -v mvn &> /dev/null; then
        echo "❌ Maven no está instalado"
        exit 1
    fi
    log "✅ Maven $(mvn -version | head -1 | cut -d' ' -f3) detectado"
    
    # Verificar PostgreSQL
    if ! nc -z localhost 5432; then
        echo "❌ PostgreSQL no está corriendo en puerto 5432"
        echo "💡 Ejecuta: sudo systemctl start postgresql"
        exit 1
    fi
    log "✅ PostgreSQL está corriendo"
}

# Función para limpiar y compilar
build_application() {
    log "🏗️ Compilando aplicación..."
    
    cd "$PROJECT_DIR"
    
    # Limpiar compilaciones anteriores
    mvn clean
    
    # Compilar sin tests para build rápido
    mvn compile -DskipTests
    
    if [ $? -eq 0 ]; then
        log "✅ Compilación exitosa"
    else
        echo "❌ Error en la compilación"
        exit 1
    fi
}

# Función para ejecutar tests (opcional)
run_tests() {
    if [ "$1" = "--skip-tests" ]; then
        log "⏭️ Saltando tests (--skip-tests especificado)"
        return 0
    fi
    
    log "🧪 Ejecutando tests..."
    
    # Ejecutar solo test unitarios rápidos
    mvn test -Dtest="*Unit*Test"
    
    if [ $? -eq 0 ]; then
        log "✅ Tests ejecutados exitosamente"
    else
        echo "⚠️ Algunos tests fallaron, pero continuando deploy..."
    fi
}

# Función para crear JAR
package_application() {
    log "📦 Empaquetando aplicación..."
    
    mvn package -DskipTests
    
    if [ $? -eq 0 ]; then
        log "✅ JAR creado exitosamente"
        ls -la target/*.jar
    else
        echo "❌ Error al crear JAR"
        exit 1
    fi
}

# Función para verificar si la aplicación ya está corriendo
check_running_app() {
    if nc -z localhost $PORT 2>/dev/null; then
        log "⚠️ Aplicación ya está corriendo en puerto $PORT"
        read -p "¿Quieres detenerla y continuar? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            log "🛑 Deteniendo aplicación actual..."
            pkill -f "spring-boot" || true
            sleep 3
        else
            echo "❌ Deployment cancelado"
            exit 1
        fi
    fi
}

# Función para iniciar la aplicación
start_application() {
    log "🚀 Iniciando aplicación..."
    
    JAR_FILE="target/${APP_NAME}-${APP_VERSION}.jar"
    
    if [ ! -f "$JAR_FILE" ]; then
        echo "❌ JAR no encontrado: $JAR_FILE"
        exit 1
    fi
    
    # Crear directorio de logs si no existe
    mkdir -p logs
    
    # Iniciar aplicación en background
    nohup java -jar "$JAR_FILE" \
        --server.port=$PORT \
        --spring.profiles.active=dev \
        > logs/application.log 2>&1 &
    
    APP_PID=$!
    echo $APP_PID > application.pid
    
    log "🔄 Aplicación iniciando (PID: $APP_PID)..."
    
    # Esperar que la aplicación inicie
    for i in {1..30}; do
        if nc -z localhost $PORT 2>/dev/null; then
            log "✅ Aplicación iniciada exitosamente en puerto $PORT"
            return 0
        fi
        echo -n "."
        sleep 2
    done
    
    echo ""
    echo "❌ La aplicación no pudo iniciar en 60 segundos"
    echo "📋 Verificar logs en: logs/application.log"
    exit 1
}

# Función para verificar health check
health_check() {
    log "🏥 Verificando health check..."
    
    sleep 5  # Esperar que la aplicación esté completamente lista
    
    health_response=$(curl -s http://localhost:$PORT/actuator/health || echo "ERROR")
    
    if [[ $health_response == *"UP"* ]]; then
        log "✅ Health check exitoso"
        echo "🌐 Aplicación disponible en: http://localhost:$PORT"
        echo "🏥 Health endpoint: http://localhost:$PORT/actuator/health"
    else
        echo "❌ Health check falló"
        echo "📋 Response: $health_response"
        echo "📋 Verificar logs en: logs/application.log"
        exit 1
    fi
}

# Función para mostrar información post-deployment
show_deployment_info() {
    log "🎉 Deployment completado exitosamente!"
    echo "================================================="
    echo "📊 Información del Deployment:"
    echo "  🏠 Directorio: $PROJECT_DIR"
    echo "  📦 JAR: target/${APP_NAME}-${APP_VERSION}.jar"
    echo "  🔗 URL: http://localhost:$PORT"
    echo "  🏥 Health: http://localhost:$PORT/actuator/health"
    echo "  📋 Logs: logs/application.log"
    echo "  🆔 PID: $(cat application.pid)"
    echo ""
    echo "🧪 Scripts de testing disponibles:"
    echo "  ./test-guide.sh - Guía completa de testing"
    echo "  ./init-test-data.sh - Inicializar datos de prueba"
    echo ""
    echo "📚 Documentación:"
    echo "  README.md - Documentación completa"
    echo "  Restaurant_API_Collection.postman_collection.json - Colección Postman"
    echo ""
    echo "🛑 Para detener la aplicación:"
    echo "  kill \$(cat application.pid)"
    echo "================================================="
}

# Función principal
main() {
    echo "Opciones disponibles:"
    echo "  --skip-tests    Saltar ejecución de tests"
    echo "  --quick         Build rápido (skip tests + package directo)"
    echo ""
    
    case "$1" in
        "--quick")
            log "⚡ Modo rápido activado"
            check_prerequisites
            check_running_app
            mvn clean package -DskipTests
            start_application
            health_check
            show_deployment_info
            ;;
        *)
            check_prerequisites
            build_application
            run_tests "$1"
            package_application
            check_running_app
            start_application
            health_check
            show_deployment_info
            ;;
    esac
}

# Ejecutar función principal con todos los argumentos
main "$@"