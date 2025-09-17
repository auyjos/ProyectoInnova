package com.innova.restaurant.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Punto de entrada para manejar errores de autenticación JWT
 * 
 * Se ejecuta cuando un usuario no autenticado intenta acceder a un recurso protegido.
 * Devuelve una respuesta JSON con información del error en lugar de redirigir
 * a una página de login (comportamiento tradicional de Spring Security).
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        logger.error("Error de autenticación: {}", authException.getMessage());
        
        // Configurar la respuesta HTTP
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        // Crear el cuerpo de la respuesta JSON
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", "Acceso denegado: Token JWT inválido o expirado");
        errorResponse.put("path", request.getRequestURI());
        
        // Información adicional para debugging (solo en desarrollo)
        if (logger.isDebugEnabled()) {
            errorResponse.put("details", authException.getMessage());
            errorResponse.put("method", request.getMethod());
        }
        
        // Escribir la respuesta JSON
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}