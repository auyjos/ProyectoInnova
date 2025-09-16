package com.innova.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad básica
 * Por ahora permite acceso libre para poder probar la funcionalidad
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean para el encodificador de contraseñas
     * 
     * @return BCryptPasswordEncoder para encriptar contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de seguridad - permite acceso libre por ahora
     * 
     * @param http configuración de seguridad HTTP
     * @return SecurityFilterChain configurado
     * @throws Exception si hay problemas en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}