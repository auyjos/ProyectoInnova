package com.innova.restaurant.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 * Configuración de MongoDB
 */
@Configuration
@ConditionalOnProperty(name = "app.database.type", havingValue = "mongo")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "restaurant_db";
    }
}