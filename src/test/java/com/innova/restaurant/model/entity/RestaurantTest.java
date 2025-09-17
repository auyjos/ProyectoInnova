package com.innova.restaurant.model.entity;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para la entidad Restaurant
 * Valida lógica de negocio y comportamiento del modelo
 */
class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
    }

    @Test
    void createRestaurant_WithValidData_SetsPropertiesCorrectly() {
        // Given
        String name = "Test Restaurant";
        String address = "123 Test Street";
        String phone = "+1234567890";
        String email = "test@restaurant.com";
        Integer maxCapacity = 50;

        // When
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhone(phone);
        restaurant.setEmail(email);
        restaurant.setMaxCapacity(maxCapacity);

        // Then
        assertEquals(name, restaurant.getName());
        assertEquals(address, restaurant.getAddress());
        assertEquals(phone, restaurant.getPhone());
        assertEquals(email, restaurant.getEmail());
        assertEquals(maxCapacity, restaurant.getMaxCapacity());
    }

    @Test
    void setMaxCapacity_ValidCapacity_UpdatesCapacity() {
        // Given
        Integer capacity = 100;

        // When
        restaurant.setMaxCapacity(capacity);

        // Then
        assertEquals(capacity, restaurant.getMaxCapacity());
    }

    @Test
    void setMaxCapacity_ZeroCapacity_UpdatesCapacity() {
        // Given
        Integer capacity = 0;

        // When
        restaurant.setMaxCapacity(capacity);

        // Then
        assertEquals(capacity, restaurant.getMaxCapacity());
    }

    @Test
    void setEmail_ValidEmail_UpdatesEmail() {
        // Given
        String email = "newemail@restaurant.com";

        // When
        restaurant.setEmail(email);

        // Then
        assertEquals(email, restaurant.getEmail());
    }

    @Test
    void setAddress_ValidAddress_UpdatesAddress() {
        // Given
        String address = "456 New Address Ave";

        // When
        restaurant.setAddress(address);

        // Then
        assertEquals(address, restaurant.getAddress());
    }

    @Test
    void setPhone_ValidPhone_UpdatesPhone() {
        // Given
        String phone = "+9876543210";

        // When
        restaurant.setPhone(phone);

        // Then
        assertEquals(phone, restaurant.getPhone());
    }

    @Test
    void setName_ValidName_UpdatesName() {
        // Given
        String name = "New Restaurant Name";

        // When
        restaurant.setName(name);

        // Then
        assertEquals(name, restaurant.getName());
    }

    @Test
    void defaultConstructor_CreatesRestaurantWithNullValues() {
        // Given & When
        Restaurant newRestaurant = new Restaurant();

        // Then
        assertNull(newRestaurant.getId());
        assertNull(newRestaurant.getName());
        assertNull(newRestaurant.getAddress());
        assertNull(newRestaurant.getPhone());
        assertNull(newRestaurant.getEmail());
        assertNull(newRestaurant.getMaxCapacity());
    }

    @Test
    void setDescription_ValidDescription_UpdatesDescription() {
        // Given
        String description = "A wonderful restaurant with great food";

        // When
        restaurant.setDescription(description);

        // Then
        assertEquals(description, restaurant.getDescription());
    }

    @Test
    void setOwner_ValidOwner_UpdatesOwner() {
        // Given
        User owner = new User();
        owner.setUsername("restaurant_owner");

        // When
        restaurant.setOwner(owner);

        // Then
        assertEquals(owner, restaurant.getOwner());
        assertEquals("restaurant_owner", restaurant.getOwner().getUsername());
    }

    @Test
    void setOpeningTime_ValidTime_UpdatesOpeningTime() {
        // Given
        LocalTime openingTime = LocalTime.of(9, 0);

        // When
        restaurant.setOpeningTime(openingTime);

        // Then
        assertEquals(openingTime, restaurant.getOpeningTime());
    }

    @Test
    void setClosingTime_ValidTime_UpdatesClosingTime() {
        // Given
        LocalTime closingTime = LocalTime.of(22, 0);

        // When
        restaurant.setClosingTime(closingTime);

        // Then
        assertEquals(closingTime, restaurant.getClosingTime());
    }

    @Test
    void setIsActive_ValidBoolean_UpdatesActiveStatus() {
        // Given
        restaurant.setIsActive(true);

        // When & Then
        assertTrue(restaurant.getIsActive());

        // When
        restaurant.setIsActive(false);

        // Then
        assertFalse(restaurant.getIsActive());
    }
}