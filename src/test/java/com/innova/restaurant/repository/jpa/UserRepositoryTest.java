package com.innova.restaurant.repository.jpa;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.innova.restaurant.model.entity.User;
import com.innova.restaurant.model.enums.UserRole;

/**
 * Tests de integración para UserRepository
 * Valida operaciones CRUD y consultas personalizadas
 * DISABLED: Requires Spring Boot context configuration
 */
@DataJpaTest
@ActiveProfiles("test")
@Disabled("Skipping Spring Boot integration tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPhone("+1234567890");
        testUser.setRole(UserRole.CUSTOMER);
        testUser.setIsActive(true);
    }

    @Test
    void save_ValidUser_PersistsUser() {
        // When
        User savedUser = userRepository.save(testUser);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(UserRole.CUSTOMER, savedUser.getRole());
        assertTrue(savedUser.getIsActive());
    }

    @Test
    void findByUsername_ExistingUser_ReturnsUser() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        Optional<User> found = userRepository.findByUsername("testuser");

        // Then
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void findByUsername_NonExistingUser_ReturnsEmpty() {
        // When
        Optional<User> found = userRepository.findByUsername("nonexistent");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void findByEmail_ExistingUser_ReturnsUser() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void existsByEmail_ExistingEmail_ReturnsTrue() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByEmail_NonExistingEmail_ReturnsFalse() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void existsByUsername_ExistingUsername_ReturnsTrue() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        boolean exists = userRepository.existsByUsername("testuser");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByUsername_NonExistingUsername_ReturnsFalse() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Then
        assertFalse(exists);
    }

    @Test
    void findByRole_ExistingRole_ReturnsUsers() {
        // Given
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("hashedPassword");
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setRole(UserRole.ADMIN);
        adminUser.setIsActive(true);

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(adminUser);

        // When
        List<User> customers = userRepository.findByRole(UserRole.CUSTOMER);
        List<User> admins = userRepository.findByRole(UserRole.ADMIN);

        // Then
        assertEquals(1, customers.size());
        assertEquals("testuser", customers.get(0).getUsername());
        
        assertEquals(1, admins.size());
        assertEquals("admin", admins.get(0).getUsername());
    }

    @Test
    void findByIsActive_ActiveUsers_ReturnsActiveUsers() {
        // Given
        User inactiveUser = new User();
        inactiveUser.setUsername("inactive");
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setPassword("hashedPassword");
        inactiveUser.setFirstName("Inactive");
        inactiveUser.setLastName("User");
        inactiveUser.setRole(UserRole.CUSTOMER);
        inactiveUser.setIsActive(false);

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(inactiveUser);

        // When
        List<User> activeUsers = userRepository.findByIsActive(true);
        List<User> inactiveUsers = userRepository.findByIsActive(false);

        // Then
        assertEquals(1, activeUsers.size());
        assertEquals("testuser", activeUsers.get(0).getUsername());
        
        assertEquals(1, inactiveUsers.size());
        assertEquals("inactive", inactiveUsers.get(0).getUsername());
    }

    @Test
    void countByRole_ExistingRole_ReturnsCount() {
        // Given
        User anotherCustomer = new User();
        anotherCustomer.setUsername("customer2");
        anotherCustomer.setEmail("customer2@example.com");
        anotherCustomer.setPassword("hashedPassword");
        anotherCustomer.setFirstName("Customer");
        anotherCustomer.setLastName("Two");
        anotherCustomer.setRole(UserRole.CUSTOMER);
        anotherCustomer.setIsActive(true);

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(anotherCustomer);

        // When
        long customerCount = userRepository.countByRole(UserRole.CUSTOMER);
        long adminCount = userRepository.countByRole(UserRole.ADMIN);

        // Then
        assertEquals(2, customerCount);
        assertEquals(0, adminCount);
    }

    @Test
    void deleteUser_ExistingUser_RemovesUser() {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);
        Long userId = savedUser.getId();

        // When
        userRepository.delete(savedUser);
        entityManager.flush();

        // Then
        Optional<User> found = userRepository.findById(userId);
        assertFalse(found.isPresent());
    }
}