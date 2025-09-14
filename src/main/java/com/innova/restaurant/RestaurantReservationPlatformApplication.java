package com.innova.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Clase principal de la aplicación Restaurant Reservation Platform
 * 
 * Esta aplicación implementa un backend para una plataforma de reservas de restaurantes
 * utilizando Spring Boot con soporte para MongoDB y PostgreSQL.
 * 
 * @author Jose
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.innova.restaurant.repository.jpa")
@EnableMongoRepositories(basePackages = "com.innova.restaurant.repository.mongo")
public class RestaurantReservationPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantReservationPlatformApplication.class, args);
    }
}