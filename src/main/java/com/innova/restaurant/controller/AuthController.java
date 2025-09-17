package com.innova.restaurant.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innova.restaurant.model.entity.User;
import com.innova.restaurant.model.enums.UserRole;
import com.innova.restaurant.repository.jpa.UserRepository;
import com.innova.restaurant.security.JwtUtil;
import com.innova.restaurant.service.impl.UserDetailsServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Controlador REST para autenticación y autorización
 * 
 * Implementa endpoints para:
 * - Login con credenciales
 * - Registro de nuevos usuarios
 * - Refresh de tokens JWT
 * - Logout (invalidación de tokens)
 * 
 * Sigue el estándar Richardson Level 2 con:
 * - Métodos HTTP apropiados (POST)
 * - Códigos de estado HTTP correctos
 * - Respuestas JSON estructuradas
 * - Validación de entrada
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Request DTO para login
     */
    public static class LoginRequest {
        @NotBlank(message = "El nombre de usuario o email es requerido")
        private String username;

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        private String password;

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    /**
     * Request DTO para registro
     */
    public static class RegisterRequest {
        @NotBlank(message = "El nombre de usuario es requerido")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        private String username;

        @NotBlank(message = "El email es requerido")
        @Email(message = "El formato del email no es válido")
        private String email;

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
        private String password;

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        private String firstName;

        @NotBlank(message = "El apellido es requerido")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        private String lastName;

        private String phone;
        
        private UserRole role = UserRole.CUSTOMER;

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }

    /**
     * Request DTO para refresh token
     */
    public static class RefreshTokenRequest {
        @NotBlank(message = "El refresh token es requerido")
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    /**
     * Response DTO para respuestas de autenticación
     */
    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private Long expiresIn;
        private UserInfo user;

        public static class UserInfo {
            private Long id;
            private String username;
            private String email;
            private String firstName;
            private String lastName;
            private String role;

            // Constructors
            public UserInfo() {}

            public UserInfo(User user) {
                this.id = user.getId();
                this.username = user.getUsername();
                this.email = user.getEmail();
                this.firstName = user.getFirstName();
                this.lastName = user.getLastName();
                this.role = user.getRole().name();
            }

            // Getters and Setters
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            
            public String getUsername() { return username; }
            public void setUsername(String username) { this.username = username; }
            
            public String getEmail() { return email; }
            public void setEmail(String email) { this.email = email; }
            
            public String getFirstName() { return firstName; }
            public void setFirstName(String firstName) { this.firstName = firstName; }
            
            public String getLastName() { return lastName; }
            public void setLastName(String lastName) { this.lastName = lastName; }
            
            public String getRole() { return role; }
            public void setRole(String role) { this.role = role; }
        }

        // Getters and Setters
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
        
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
        
        public Long getExpiresIn() { return expiresIn; }
        public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
        
        public UserInfo getUser() { return user; }
        public void setUser(UserInfo user) { this.user = user; }
    }

    /**
     * Endpoint para autenticación de usuarios
     * 
     * POST /api/auth/login
     * 
     * @param loginRequest credenciales del usuario
     * @return token JWT y información del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Intento de login para usuario: {}", loginRequest.getUsername());

            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Obtener detalles del usuario autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Buscar el usuario en la base de datos para obtener información completa
            User user = userRepository.findByEmailOrUsername(
                loginRequest.getUsername(), 
                loginRequest.getUsername()
            ).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Generar tokens JWT
            String accessToken = jwtUtil.generateToken(userDetails.getUsername(), user.getRole().name(), user.getId());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            // Crear respuesta
            AuthResponse response = new AuthResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setExpiresIn(jwtUtil.getAccessTokenExpiration());
            response.setUser(new AuthResponse.UserInfo(user));

            logger.info("Login exitoso para usuario: {}", user.getUsername());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            logger.warn("Fallo de autenticación para usuario: {}", loginRequest.getUsername());
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            error.put("message", "Usuario o contraseña incorrectos");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * Endpoint para registro de nuevos usuarios
     * 
     * POST /api/auth/register
     * 
     * @param registerRequest datos del nuevo usuario
     * @return token JWT y información del usuario registrado
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            logger.info("Intento de registro para usuario: {}", registerRequest.getUsername());

            // Verificar si el usuario ya existe
            if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuario ya existe");
                error.put("message", "El nombre de usuario ya está en uso");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Verificar si el email ya existe
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email ya existe");
                error.put("message", "El email ya está registrado");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Crear nuevo usuario
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setFirstName(registerRequest.getFirstName());
            newUser.setLastName(registerRequest.getLastName());
            newUser.setPhone(registerRequest.getPhone());
            newUser.setRole(registerRequest.getRole()); // Usar el rol del request
            newUser.setIsActive(true);

            // Guardar usuario
            User savedUser = userRepository.save(newUser);

            // Generar tokens JWT
            String accessToken = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().name(), savedUser.getId());
            String refreshToken = jwtUtil.generateRefreshToken(savedUser.getUsername());

            // Crear respuesta
            AuthResponse response = new AuthResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setExpiresIn(jwtUtil.getAccessTokenExpiration());
            response.setUser(new AuthResponse.UserInfo(savedUser));

            logger.info("Registro exitoso para usuario: {}", savedUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error durante el registro: ", e);
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error de registro");
            error.put("message", "No se pudo completar el registro");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint para refrescar token de acceso
     * 
     * POST /api/auth/refresh
     * 
     * @param refreshRequest token de refresh
     * @return nuevo token de acceso
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshRequest) {
        try {
            String refreshToken = refreshRequest.getRefreshToken();
            
            // Validar refresh token
            if (!jwtUtil.isTokenValid(refreshToken)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Token inválido");
                error.put("message", "El refresh token es inválido o ha expirado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // Extraer username del refresh token
            String username = jwtUtil.extractUsername(refreshToken);
            
            // Buscar usuario
            User user = userRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Generar nuevo access token
            String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), user.getId());

            // Crear respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("tokenType", "Bearer");
            response.put("expiresIn", jwtUtil.getAccessTokenExpiration());

            logger.info("Token refrescado exitosamente para usuario: {}", username);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error durante el refresh de token: ", e);
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error de refresh");
            error.put("message", "No se pudo refrescar el token");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * Endpoint para logout (invalidación de token)
     * 
     * POST /api/auth/logout
     * 
     * @param authHeader header de autorización con el token
     * @return confirmación de logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            // En una implementación real, aquí se invalidaría el token
            // guardándolo en una blacklist en Redis o base de datos
            
            logger.info("Logout ejecutado");
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout exitoso");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error durante el logout: ", e);
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error de logout");
            error.put("message", "No se pudo completar el logout");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}