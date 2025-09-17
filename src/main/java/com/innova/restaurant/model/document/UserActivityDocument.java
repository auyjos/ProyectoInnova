package com.innova.restaurant.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Documento UserActivity para MongoDB
 * Almacena logs de actividad de usuarios del sistema
 */
@Document(collection = "user_activities")
public class UserActivityDocument {

    @Id
    private String id;

    // Referencia al usuario (ID de PostgreSQL)
    @Field("user_id")
    private Long userId;

    // Información del evento
    @Field("action_type")
    private String actionType; // "login", "logout", "reservation_created", "review_posted", "profile_updated", etc.

    @Field("description")
    private String description;

    @Field("resource_type")
    private String resourceType; // "user", "restaurant", "reservation", "review"

    @Field("resource_id")
    private String resourceId; // ID del recurso afectado

    // Detalles técnicos
    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("session_id")
    private String sessionId;

    // Información adicional flexible
    @Field("metadata")
    private Map<String, Object> metadata; // Datos adicionales específicos por acción

    // Geolocalización (opcional)
    @Field("location")
    private LocationInfo location;

    // Resultado de la acción
    @Field("status")
    private String status; // "success", "failed", "pending"

    @Field("error_message")
    private String errorMessage; // Si la acción falló

    // Timestamps
    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("duration_ms")
    private Long durationMs; // Duración de la acción en milisegundos

    // Información de auditoría
    @Field("severity_level")
    private String severityLevel; // "info", "warning", "error", "critical"

    @Field("is_sensitive")
    private Boolean isSensitive = false; // Si contiene información sensible

    // Clase interna para información de geolocalización
    public static class LocationInfo {
        private String country;
        private String city;
        private String region;
        private Double latitude;
        private Double longitude;

        // Constructores
        public LocationInfo() {}

        public LocationInfo(String country, String city, String region) {
            this.country = country;
            this.city = city;
            this.region = region;
        }

        // Getters y Setters
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }

    // Constructores
    public UserActivityDocument() {
        this.timestamp = LocalDateTime.now();
        this.severityLevel = "info";
        this.status = "success";
    }

    public UserActivityDocument(Long userId, String actionType, String description) {
        this();
        this.userId = userId;
        this.actionType = actionType;
        this.description = description;
    }

    // Métodos de utilidad
    public void markAsCompleted(Long durationMs) {
        this.status = "success";
        this.durationMs = durationMs;
    }

    public void markAsFailed(String errorMessage) {
        this.status = "failed";
        this.errorMessage = errorMessage;
        this.severityLevel = "error";
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public LocationInfo getLocation() { return location; }
    public void setLocation(LocationInfo location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }

    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }

    public Boolean getIsSensitive() { return isSensitive; }
    public void setIsSensitive(Boolean isSensitive) { this.isSensitive = isSensitive; }
}