package com.innova.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuración de base de datos para la aplicación
 * 
 * Esta clase permite la configuración dinámica entre MongoDB y PostgreSQL
 * utilizando el principio de Inversión de Control.
 */
@Configuration
public class DatabaseConfig {

    @Value("${app.database.type:jpa}")
    private String databaseType;

    /**
     * Obtiene el tipo de base de datos configurado
     * 
     * @return el tipo de base de datos (jpa o mongo)
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * Verifica si se está utilizando JPA (PostgreSQL)
     * 
     * @return true si se está utilizando JPA
     */
    public boolean isJpaEnabled() {
        return "jpa".equalsIgnoreCase(databaseType);
    }

    /**
     * Verifica si se está utilizando MongoDB
     * 
     * @return true si se está utilizando MongoDB
     */
    public boolean isMongoEnabled() {
        return "mongo".equalsIgnoreCase(databaseType);
    }
}