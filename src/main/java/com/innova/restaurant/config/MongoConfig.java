package com.innova.restaurant.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 * Configuración de MongoDB
 */
@Configuration
@ConditionalOnProperty(name = "app.database.type", havingValue = "mongo")
@Profile("mongo")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${MONGODB_URI:mongodb://localhost:27017/restaurant_db}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        // Extraer el nombre de la base de datos del URI
        if (mongoUri.contains("/restaurant_db")) {
            return "restaurant_db";
        }
        // Fallback por defecto
        return "restaurant_db";
    }
    
    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        // Configurar el URI de conexión
        builder.applyConnectionString(new ConnectionString(mongoUri));
        System.out.println("🔧 MongoDB configurado con URI: " + mongoUri);
    }
}