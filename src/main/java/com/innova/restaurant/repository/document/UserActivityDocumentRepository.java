package com.innova.restaurant.repository.document;

import com.innova.restaurant.model.document.UserActivityDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para UserActivityDocument utilizando query methods sin @Query
 */
@Repository
public interface UserActivityDocumentRepository extends MongoRepository<UserActivityDocument, String> {

    // Query methods sin @Query - siguiendo el patrón establecido

    // Buscar por usuario
    List<UserActivityDocument> findByUserId(Long userId);
    Page<UserActivityDocument> findByUserId(Long userId, Pageable pageable);

    // Buscar por tipo de acción
    List<UserActivityDocument> findByActionType(String actionType);
    Page<UserActivityDocument> findByActionType(String actionType, Pageable pageable);

    // Buscar por estado
    List<UserActivityDocument> findByStatus(String status);
    List<UserActivityDocument> findByStatusIn(List<String> statuses);

    // Buscar por nivel de severidad
    List<UserActivityDocument> findBySeverityLevel(String severityLevel);
    List<UserActivityDocument> findBySeverityLevelIn(List<String> severityLevels);

    // Buscar actividades sensibles
    List<UserActivityDocument> findByIsSensitiveTrue();
    List<UserActivityDocument> findByIsSensitiveFalse();

    // Buscar por fecha
    List<UserActivityDocument> findByTimestampAfter(LocalDateTime date);
    List<UserActivityDocument> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<UserActivityDocument> findByTimestampBefore(LocalDateTime date);

    // Buscar por IP
    List<UserActivityDocument> findByIpAddress(String ipAddress);
    List<UserActivityDocument> findByIpAddressContaining(String ipPattern);

    // Buscar por sesión
    List<UserActivityDocument> findBySessionId(String sessionId);

    // Buscar por tipo de recurso
    List<UserActivityDocument> findByResourceType(String resourceType);
    List<UserActivityDocument> findByResourceTypeAndResourceId(String resourceType, String resourceId);

    // Queries complejas combinadas
    List<UserActivityDocument> findByUserIdAndActionType(Long userId, String actionType);
    List<UserActivityDocument> findByUserIdAndTimestampAfter(Long userId, LocalDateTime date);
    List<UserActivityDocument> findByUserIdAndStatus(Long userId, String status);
    List<UserActivityDocument> findByActionTypeAndStatus(String actionType, String status);
    List<UserActivityDocument> findByActionTypeAndTimestampBetween(String actionType, LocalDateTime startDate, LocalDateTime endDate);

    // Buscar actividades fallidas
    List<UserActivityDocument> findByStatusAndErrorMessageIsNotNull(String status);
    List<UserActivityDocument> findByStatusAndTimestampAfter(String status, LocalDateTime date);

    // Buscar actividades por duración
    List<UserActivityDocument> findByDurationMsGreaterThan(Long durationMs);
    List<UserActivityDocument> findByDurationMsBetween(Long minDuration, Long maxDuration);

    // Buscar actividades recientes
    List<UserActivityDocument> findTop10ByUserIdOrderByTimestampDesc(Long userId);
    List<UserActivityDocument> findTop20ByActionTypeOrderByTimestampDesc(String actionType);
    List<UserActivityDocument> findTop50ByStatusOrderByTimestampDesc(String status);

    // Buscar por geolocalización (si está disponible)
    List<UserActivityDocument> findByLocationCountry(String country);
    List<UserActivityDocument> findByLocationCity(String city);
    List<UserActivityDocument> findByLocationCountryAndLocationCity(String country, String city);

    // Contar actividades
    long countByUserId(Long userId);
    long countByActionType(String actionType);
    long countByStatus(String status);
    long countByUserIdAndActionType(Long userId, String actionType);
    long countByUserIdAndTimestampAfter(Long userId, LocalDateTime date);
    long countByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar errores y problemas
    List<UserActivityDocument> findBySeverityLevelInOrderByTimestampDesc(List<String> severityLevels);
    List<UserActivityDocument> findByStatusAndSeverityLevel(String status, String severityLevel);

    // Auditoría de actividades sensibles
    List<UserActivityDocument> findByIsSensitiveTrueAndTimestampAfter(LocalDateTime date);
    Page<UserActivityDocument> findByIsSensitiveTrueAndTimestampAfter(LocalDateTime date, Pageable pageable);

    // Buscar por User Agent para detectar patrones
    List<UserActivityDocument> findByUserAgentContainingIgnoreCase(String userAgentPattern);

    // Actividades por rango de tiempo y usuario específico
    Page<UserActivityDocument> findByUserIdAndTimestampBetweenOrderByTimestampDesc(
        Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Buscar actividades de login/logout
    List<UserActivityDocument> findByUserIdAndActionTypeInOrderByTimestampDesc(Long userId, List<String> actionTypes);

    // Últimas actividades por tipo
    List<UserActivityDocument> findByActionTypeAndTimestampAfterOrderByTimestampDesc(String actionType, LocalDateTime since);
}