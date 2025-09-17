package com.innova.restaurant.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.innova.restaurant.model.enums.UserRole;

/**
 * Tests unitarios para la entidad User
 * Valida lógica de negocio y comportamiento del modelo
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void createUser_WithValidData_SetsPropertiesCorrectly() {
        // Given
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";
        String firstName = "Test";
        String lastName = "User";
        UserRole role = UserRole.CUSTOMER;

        // When
        User newUser = new User(username, email, password, firstName, lastName, role);

        // Then
        assertEquals(username, newUser.getUsername());
        assertEquals(email, newUser.getEmail());
        assertEquals(password, newUser.getPassword());
        assertEquals(firstName, newUser.getFirstName());
        assertEquals(lastName, newUser.getLastName());
        assertEquals(role, newUser.getRole());
        assertTrue(newUser.getIsActive()); // Default should be true
    }

    @Test
    void setIsActive_ValidBoolean_UpdatesActiveStatus() {
        // Given
        user.setIsActive(true);

        // When & Then
        assertTrue(user.getIsActive());

        // When
        user.setIsActive(false);

        // Then
        assertFalse(user.getIsActive());
    }

    @Test
    void setRole_ValidRole_UpdatesRole() {
        // Given & When
        user.setRole(UserRole.RESTAURANT_OWNER);

        // Then
        assertEquals(UserRole.RESTAURANT_OWNER, user.getRole());

        // When
        user.setRole(UserRole.ADMIN);

        // Then
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void setEmail_ValidEmail_UpdatesEmail() {
        // Given
        String email = "new@example.com";

        // When
        user.setEmail(email);

        // Then
        assertEquals(email, user.getEmail());
    }

    @Test
    void setPhone_ValidPhone_UpdatesPhone() {
        // Given
        String phone = "+1234567890";

        // When
        user.setPhone(phone);

        // Then
        assertEquals(phone, user.getPhone());
    }

    @Test
    void defaultConstructor_CreatesUserWithNullValues() {
        // Given & When
        User newUser = new User();

        // Then
        assertNull(newUser.getId());
        assertNull(newUser.getUsername());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
        assertNull(newUser.getFirstName());
        assertNull(newUser.getLastName());
        assertNull(newUser.getPhone());
        // These have default values in the entity
        assertEquals(UserRole.CUSTOMER, newUser.getRole()); // Default role
        assertTrue(newUser.getIsActive()); // Default is true
    }

    @Test
    void setUsername_ValidUsername_UpdatesUsername() {
        // Given
        String username = "newusername";

        // When
        user.setUsername(username);

        // Then
        assertEquals(username, user.getUsername());
    }

    @Test
    void setFirstName_ValidName_UpdatesFirstName() {
        // Given
        String firstName = "John";

        // When
        user.setFirstName(firstName);

        // Then
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    void setLastName_ValidName_UpdatesLastName() {
        // Given
        String lastName = "Doe";

        // When
        user.setLastName(lastName);

        // Then
        assertEquals(lastName, user.getLastName());
    }

    @Test
    void userRoles_AllRolesAvailable() {
        // Test that all required roles are available
        assertNotNull(UserRole.CUSTOMER);
        assertNotNull(UserRole.RESTAURANT_OWNER);
        assertNotNull(UserRole.ADMIN);
        
        assertEquals(3, UserRole.values().length);
    }
}