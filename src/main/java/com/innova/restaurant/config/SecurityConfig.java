package com.innova.restaurant.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.innova.restaurant.security.JwtAuthenticationEntryPoint;
import com.innova.restaurant.security.JwtAuthenticationFilter;
import com.innova.restaurant.service.impl.UserDetailsServiceImpl;

/**
 * Configuración de seguridad Spring Security con JWT
 * 
 * Características implementadas:
 * - Autenticación JWT sin estado (stateless)
 * - Configuración CORS para desarrollo
 * - Protección de endpoints según roles
 * - Puntos de entrada no autenticados para login y registro
 * - Integración con UserDetailsService personalizado
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Configuración del filtro de seguridad
     * Define qué endpoints están protegidos y cuáles son públicos
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF ya que usamos JWT
            .csrf(csrf -> csrf.disable())
            
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configurar manejo de excepciones de autenticación
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            
            // Configurar política de sesiones (sin estado)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configurar autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos - no requieren autenticación
                .requestMatchers(
                    "/api/auth/**",           // Login, registro, refresh token
                    "/api/restaurants/search", // Búsqueda pública de restaurantes
                    "/api/restaurants/{id}",   // Ver detalles públicos de restaurante
                    "/api/restaurants",        // Listar restaurantes (público)
                    "/actuator/health",        // Health check
                    "/swagger-ui/**",          // Documentación Swagger
                    "/v3/api-docs/**",         // OpenAPI docs
                    "/error"                   // Manejo de errores
                ).permitAll()
                
                // Endpoints específicos para PROPIETARIOS
                .requestMatchers(
                    "/api/restaurants/create",
                    "/api/restaurants/*/manage",
                    "/api/restaurants/*/status"
                ).hasRole("RESTAURANT_OWNER")
                
                // Endpoints específicos para ADMINISTRADORES
                .requestMatchers(
                    "/api/admin/**",
                    "/api/users/*/role"
                ).hasRole("ADMIN")
                
                // Endpoints que requieren autenticación pero cualquier rol
                .requestMatchers(
                    "/api/reservations/**",
                    "/api/profile/**"
                ).authenticated()
                
                // Todos los demás endpoints requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Agregar filtro JWT antes del filtro de autenticación por username/password
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración del proveedor de autenticación DAO
     * Conecta el UserDetailsService con el encoder de contraseñas
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean del AuthenticationManager
     * Necesario para el proceso de autenticación en AuthController
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Encoder de contraseñas usando BCrypt
     * BCrypt es el estándar recomendado para hash de contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración CORS para permitir peticiones desde frontend
     * En producción se debe configurar con dominios específicos
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir orígenes específicos (en desarrollo: localhost)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",   // React dev server
            "http://localhost:4200",   // Angular dev server
            "http://localhost:5173"    // Vite dev server
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", 
            "Content-Type", 
            "Accept",
            "X-Requested-With",
            "Cache-Control"
        ));
        
        // Permitir credentials (cookies, auth headers)
        configuration.setAllowCredentials(true);
        
        // Headers expuestos al cliente
        configuration.setExposedHeaders(List.of("Authorization"));
        
        // Tiempo de cache para preflight requests
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}