package com.innova.restaurant.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innova.restaurant.config.DatabaseConfig;
import com.innova.restaurant.dto.CreateUserDto;
import com.innova.restaurant.dto.UpdateUserDto;
import com.innova.restaurant.dto.UserDto;
import com.innova.restaurant.exception.DuplicateResourceException;
import com.innova.restaurant.exception.ResourceNotFoundException;
import com.innova.restaurant.model.document.UserDocument;
import com.innova.restaurant.model.entity.User;
import com.innova.restaurant.model.enums.UserRole;
import com.innova.restaurant.repository.jpa.UserRepository;
import com.innova.restaurant.repository.jpa.UserSpecifications;
import com.innova.restaurant.repository.document.UserDocumentRepository;
import com.innova.restaurant.service.UserService;

/**
 * Implementación del servicio de usuario que utiliza inversión de control
 * para alternar entre JPA (PostgreSQL) y MongoDB según la configuración
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final DatabaseConfig databaseConfig;
    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(DatabaseConfig databaseConfig,
                          UserRepository userRepository,
                          UserDocumentRepository userDocumentRepository,
                          PasswordEncoder passwordEncoder) {
        this.databaseConfig = databaseConfig;
        this.userRepository = userRepository;
        this.userDocumentRepository = userDocumentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        // Validar que el email y username no existan
        if (existsByEmail(createUserDto.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + createUserDto.getEmail());
        }
        
        if (existsByUsername(createUserDto.getUsername())) {
            throw new DuplicateResourceException("Ya existe un usuario con el username: " + createUserDto.getUsername());
        }

        // Encriptar contraseña
        String encodedPassword = passwordEncoder.encode(createUserDto.getPassword());

        if (databaseConfig.isJpaEnabled()) {
            return createUserJpa(createUserDto, encodedPassword);
        } else {
            return createUserMongo(createUserDto, encodedPassword);
        }
    }

    @Override
    public UserDto updateUser(String id, UpdateUserDto updateUserDto) {
        if (databaseConfig.isJpaEnabled()) {
            return updateUserJpa(id, updateUserDto);
        } else {
            return updateUserMongo(id, updateUserDto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(String id) {
        if (databaseConfig.isJpaEnabled()) {
            return getUserByIdJpa(id);
        } else {
            return getUserByIdMongo(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByEmail(String email) {
        if (databaseConfig.isJpaEnabled()) {
            return userRepository.findByEmail(email).map(this::convertToDto);
        } else {
            return userDocumentRepository.findByEmail(email).map(this::convertToDto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByUsername(String username) {
        if (databaseConfig.isJpaEnabled()) {
            return userRepository.findByUsername(username).map(this::convertToDto);
        } else {
            return userDocumentRepository.findByUsername(username).map(this::convertToDto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        if (databaseConfig.isJpaEnabled()) {
            return userRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            return userDocumentRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(UserRole role) {
        if (databaseConfig.isJpaEnabled()) {
            return userRepository.findByRole(role).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            return userDocumentRepository.findByRole(role).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByActiveStatus(Boolean isActive) {
        if (databaseConfig.isJpaEnabled()) {
            return userRepository.findByIsActive(isActive).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            return userDocumentRepository.findByIsActive(isActive).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> searchUsersByName(String searchTerm) {
        if (databaseConfig.isJpaEnabled()) {
            // Usar método derivado del repositorio JPA - SIN @Query
            return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                    searchTerm, searchTerm).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            // Usar método derivado del repositorio MongoDB - SIN @Query
            return userDocumentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                    searchTerm, searchTerm).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean deleteUser(String id) {
        if (databaseConfig.isJpaEnabled()) {
            return deleteUserJpa(id);
        } else {
            return deleteUserMongo(id);
        }
    }

    @Override
    public UserDto toggleUserActiveStatus(String id, Boolean isActive) {
        if (databaseConfig.isJpaEnabled()) {
            return toggleUserActiveStatusJpa(id, isActive);
        } else {
            return toggleUserActiveStatusMongo(id, isActive);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (databaseConfig.isJpaEnabled()) {
            return userRepository.existsByEmail(email);
        } else {
            return userDocumentRepository.existsByEmail(email);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        if (databaseConfig.isJpaEnabled()) {
            return userRepository.existsByUsername(username);
        } else {
            return userDocumentRepository.existsByUsername(username);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByRole(UserRole role) {
        if (databaseConfig.isJpaEnabled()) {
            return userRepository.countByRole(role);
        } else {
            return userDocumentRepository.countByRole(role);
        }
    }

    // Métodos privados para JPA
    private UserDto createUserJpa(CreateUserDto createUserDto, String encodedPassword) {
        User user = new User(
                createUserDto.getUsername(),
                createUserDto.getEmail(),
                encodedPassword,
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getRole()
        );
        user.setPhone(createUserDto.getPhone());
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    private UserDto createUserMongo(CreateUserDto createUserDto, String encodedPassword) {
        UserDocument userDocument = new UserDocument(
                createUserDto.getUsername(),
                createUserDto.getEmail(),
                encodedPassword,
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getRole()
        );
        userDocument.setPhone(createUserDto.getPhone());
        
        UserDocument savedUser = userDocumentRepository.save(userDocument);
        return convertToDto(savedUser);
    }

    private UserDto updateUserJpa(String id, UpdateUserDto updateUserDto) {
        Long userId = Long.valueOf(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        updateUserFields(user, updateUserDto);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    private UserDto updateUserMongo(String id, UpdateUserDto updateUserDto) {
        UserDocument userDocument = userDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        updateUserFields(userDocument, updateUserDto);
        userDocument.updateTimestamp();
        UserDocument updatedUser = userDocumentRepository.save(userDocument);
        return convertToDto(updatedUser);
    }

    private Optional<UserDto> getUserByIdJpa(String id) {
        try {
            Long userId = Long.valueOf(id);
            return userRepository.findById(userId).map(this::convertToDto);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<UserDto> getUserByIdMongo(String id) {
        return userDocumentRepository.findById(id).map(this::convertToDto);
    }

    private boolean deleteUserJpa(String id) {
        try {
            Long userId = Long.valueOf(id);
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean deleteUserMongo(String id) {
        if (userDocumentRepository.existsById(id)) {
            userDocumentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private UserDto toggleUserActiveStatusJpa(String id, Boolean isActive) {
        Long userId = Long.valueOf(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        user.setIsActive(isActive);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    private UserDto toggleUserActiveStatusMongo(String id, Boolean isActive) {
        UserDocument userDocument = userDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        userDocument.setIsActive(isActive);
        userDocument.updateTimestamp();
        UserDocument updatedUser = userDocumentRepository.save(userDocument);
        return convertToDto(updatedUser);
    }

    // Métodos de utilidad para actualizar campos
    private void updateUserFields(User user, UpdateUserDto updateUserDto) {
        if (updateUserDto.getUsername() != null) {
            user.setUsername(updateUserDto.getUsername());
        }
        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getFirstName() != null) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null) {
            user.setLastName(updateUserDto.getLastName());
        }
        if (updateUserDto.getPhone() != null) {
            user.setPhone(updateUserDto.getPhone());
        }
    }

    private void updateUserFields(UserDocument userDocument, UpdateUserDto updateUserDto) {
        if (updateUserDto.getUsername() != null) {
            userDocument.setUsername(updateUserDto.getUsername());
        }
        if (updateUserDto.getEmail() != null) {
            userDocument.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getFirstName() != null) {
            userDocument.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null) {
            userDocument.setLastName(updateUserDto.getLastName());
        }
        if (updateUserDto.getPhone() != null) {
            userDocument.setPhone(updateUserDto.getPhone());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> searchUsers(String firstName, String lastName, String email, 
                                   String username, UserRole role, Boolean isActive) {
        if (databaseConfig.isJpaEnabled()) {
            // Usar Specifications para consulta dinámica
            Specification<User> spec = Specification.where(null);
            
            if (firstName != null && !firstName.trim().isEmpty()) {
                spec = spec.and(UserSpecifications.hasFirstNameContaining(firstName));
            }
            if (lastName != null && !lastName.trim().isEmpty()) {
                spec = spec.and(UserSpecifications.hasLastNameContaining(lastName));
            }
            if (email != null && !email.trim().isEmpty()) {
                spec = spec.and(UserSpecifications.hasEmailContaining(email));
            }
            if (username != null && !username.trim().isEmpty()) {
                spec = spec.and(UserSpecifications.hasUsernameContaining(username));
            }
            if (role != null) {
                spec = spec.and(UserSpecifications.hasRole(role));
            }
            if (isActive != null) {
                spec = spec.and(UserSpecifications.isActive(isActive));
            }
            
            return userRepository.findAll(spec).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            // Para MongoDB, usar búsqueda por ejemplo o implementar lógica similar
            // Por simplicidad, retornamos todos y filtramos en memoria (puede optimizarse)
            List<UserDocument> allUsers = userDocumentRepository.findAll();
            return allUsers.stream()
                    .filter(user -> firstName == null || 
                            user.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                    .filter(user -> lastName == null || 
                            user.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                    .filter(user -> email == null || 
                            user.getEmail().toLowerCase().contains(email.toLowerCase()))
                    .filter(user -> username == null || 
                            user.getUsername().toLowerCase().contains(username.toLowerCase()))
                    .filter(user -> role == null || user.getRole().equals(role))
                    .filter(user -> isActive == null || user.getIsActive().equals(isActive))
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    // Métodos de conversión a DTO
    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole(),
                user.getIsActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private UserDto convertToDto(UserDocument userDocument) {
        return new UserDto(
                userDocument.getId(),
                userDocument.getUsername(),
                userDocument.getEmail(),
                userDocument.getFirstName(),
                userDocument.getLastName(),
                userDocument.getPhone(),
                userDocument.getRole(),
                userDocument.getIsActive(),
                userDocument.getCreatedAt(),
                userDocument.getUpdatedAt()
        );
    }
}