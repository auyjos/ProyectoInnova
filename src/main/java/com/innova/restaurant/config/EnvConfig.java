package com.innova.restaurant.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Configuración para cargar variables de entorno desde archivo .env
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EnvConfig {

    static {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            // Cargar variables de entorno al sistema
            dotenv.entries().forEach(entry -> 
                System.setProperty(entry.getKey(), entry.getValue())
            );

            System.out.println("✅ Variables de entorno cargadas correctamente desde .env");
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cargar el archivo .env: " + e.getMessage());
            System.out.println("💡 Asegúrate de que el archivo .env existe en la raíz del proyecto");
        }
    }
}