package com.innova.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de base de datos para la aplicación
 * 
 * Esta clase permite la configuración dinámica entre MongoDB, PostgreSQL o modo híbrido
 * utilizando el principio de Inversión de Control.
 */
@Configuration
public class DatabaseConfig {

    @Value("${app.database.type:hybrid}")
    private String databaseType;

    /**
     * Obtiene el tipo de base de datos configurado
     * 
     * @return el tipo de base de datos (jpa, mongo, o hybrid)
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * Verifica si se está utilizando JPA (PostgreSQL)
     * 
     * @return true si se está utilizando JPA o modo híbrido
     */
    public boolean isJpaEnabled() {
        return "jpa".equalsIgnoreCase(databaseType) || "hybrid".equalsIgnoreCase(databaseType);
    }

    /**
     * Verifica si se está utilizando MongoDB
     * 
     * @return true si se está utilizando MongoDB o modo híbrido
     */
    public boolean isMongoEnabled() {
        return "mongo".equalsIgnoreCase(databaseType) || "hybrid".equalsIgnoreCase(databaseType);
    }

    /**
     * Verifica si se está utilizando el modo híbrido (PostgreSQL + MongoDB)
     * 
     * @return true si se está utilizando modo híbrido
     */
    public boolean isHybridEnabled() {
        return "hybrid".equalsIgnoreCase(databaseType);
    }
}