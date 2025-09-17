package com.innova.restaurant.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración específica para tests
 * Sobrescribe beans necesarios para el entorno de testing
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Password encoder para tests
     * Usa BCrypt con configuración optimizada para testing
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4); // Menos rounds para tests más rápidos
    }

    /**
     * Configuración de base de datos para tests
     * Fuerza el uso de JPA para consistencia en tests
     */
    @Bean
    @Primary
    public DatabaseConfig testDatabaseConfig() {
        return new DatabaseConfig() {
            @Override
            public boolean isJpaEnabled() {
                return true; // Forzar JPA en tests
            }
            
            @Override
            public boolean isMongoEnabled() {
                return false; // Deshabilitar MongoDB en tests unitarios
            }
        };
    }
}