package com.innova.restaurant.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Documento Review para MongoDB
 * Almacena reviews de usuarios hacia restaurantes con estructura flexible
 */
@Document(collection = "reviews")
public class ReviewDocument {

    @Id
    private String id;

    // Referencias a entidades PostgreSQL (usando IDs)
    @Field("user_id")
    private Long userId;

    @Field("restaurant_id")
    private Long restaurantId;

    @Field("reservation_id")
    private Long reservationId; // Opcional, si la review está asociada a una reserva

    // Información del review
    @Field("rating")
    private Integer rating; // 1-5 estrellas

    @Field("overall_rating") 
    private Double overallRating; // Rating general como double para mayor precisión

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("comment")
    private String comment; // Alias para content

    // Ratings detallados (estructura flexible) - Cambiamos a Double para mayor precision
    @Field("detailed_ratings")
    private Map<String, Double> detailedRatings; // ej: {"food": 4.5, "service": 4.0, "ambiance": 5.0, "value": 4.2}

    // Tags y categorías
    @Field("tags")
    private List<String> tags; // ej: ["romantic", "family-friendly", "noisy"]

    // Información adicional flexible
    @Field("visit_type")
    private String visitType; // "dinner", "lunch", "breakfast", "special_occasion"

    @Field("party_size")
    private Integer partySize;

    @Field("visit_date")
    private LocalDateTime visitDate;

    // Interacciones con el review
    @Field("helpful_votes")
    private Integer helpfulVotes = 0;

    @Field("total_votes")
    private Integer totalVotes = 0;

    // Respuesta del restaurante (opcional)
    @Field("restaurant_response")
    private RestaurantResponse restaurantResponse;

    // Metadatos
    @Field("is_verified")
    private Boolean isVerified = false; // Si está verificado que el usuario visitó el restaurante

    @Field("is_visible")
    private Boolean isVisible = true;

    @Field("moderation_status")
    private String moderationStatus = "approved"; // "pending", "approved", "rejected"

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    // Información del usuario en el momento del review (cache para performance)
    @Field("user_info")
    private UserInfo userInfo;

    // Información del restaurante en el momento del review (cache para performance)
    @Field("restaurant_info")
    private RestaurantInfo restaurantInfo;

    // Clase interna para respuesta del restaurante
    public static class RestaurantResponse {
        private String content;
        private String response; // Alias para content
        private LocalDateTime responseDate;
        private LocalDateTime respondedAt; // Alias para responseDate
        private String respondedBy; // Nombre del manager que respondió
        private Long responderId; // ID del manager que respondió

        // Constructores, getters y setters
        public RestaurantResponse() {}

        public RestaurantResponse(String content, String respondedBy) {
            this.content = content;
            this.response = content;
            this.respondedBy = respondedBy;
            this.responseDate = LocalDateTime.now();
            this.respondedAt = LocalDateTime.now();
        }

        public String getContent() { return content; }
        public void setContent(String content) { 
            this.content = content; 
            this.response = content;
        }

        public String getResponse() { return response; }
        public void setResponse(String response) { 
            this.response = response; 
            this.content = response;
        }

        public LocalDateTime getResponseDate() { return responseDate; }
        public void setResponseDate(LocalDateTime responseDate) { 
            this.responseDate = responseDate; 
            this.respondedAt = responseDate;
        }

        public LocalDateTime getRespondedAt() { return respondedAt; }
        public void setRespondedAt(LocalDateTime respondedAt) { 
            this.respondedAt = respondedAt; 
            this.responseDate = respondedAt;
        }

        public String getRespondedBy() { return respondedBy; }
        public void setRespondedBy(String respondedBy) { this.respondedBy = respondedBy; }

        public Long getResponderId() { return responderId; }
        public void setResponderId(Long responderId) { this.responderId = responderId; }
    }

    // Clase interna para información del usuario (cache)
    public static class UserInfo {
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private Integer totalReviews;

        // Constructors, getters y setters
        public UserInfo() {}

        public UserInfo(String firstName, String lastName, String username, String email, Integer totalReviews) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.email = email;
            this.totalReviews = totalReviews;
        }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Integer getTotalReviews() { return totalReviews; }
        public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    }

    // Clase interna para información del restaurante (cache)
    public static class RestaurantInfo {
        private String name;
        private String address;
        private String phoneNumber;
        private Double averageRating;

        // Constructors, getters y setters
        public RestaurantInfo() {}

        public RestaurantInfo(String name, String address, String phoneNumber, Double averageRating) {
            this.name = name;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.averageRating = averageRating;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    }

    // Constructores
    public ReviewDocument() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ReviewDocument(Long userId, Long restaurantId, Integer rating, String title, String content) {
        this();
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.rating = rating;
        this.title = title;
        this.content = content;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Double getOverallRating() { return overallRating; }
    public void setOverallRating(Double overallRating) { this.overallRating = overallRating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Map<String, Double> getDetailedRatings() { return detailedRatings; }
    public void setDetailedRatings(Map<String, Double> detailedRatings) { this.detailedRatings = detailedRatings; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getVisitType() { return visitType; }
    public void setVisitType(String visitType) { this.visitType = visitType; }

    public Integer getPartySize() { return partySize; }
    public void setPartySize(Integer partySize) { this.partySize = partySize; }

    public LocalDateTime getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDateTime visitDate) { this.visitDate = visitDate; }

    public Integer getHelpfulVotes() { return helpfulVotes; }
    public void setHelpfulVotes(Integer helpfulVotes) { this.helpfulVotes = helpfulVotes; }

    public Integer getTotalVotes() { return totalVotes; }
    public void setTotalVotes(Integer totalVotes) { this.totalVotes = totalVotes; }

    public RestaurantResponse getRestaurantResponse() { return restaurantResponse; }
    public void setRestaurantResponse(RestaurantResponse restaurantResponse) { this.restaurantResponse = restaurantResponse; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }

    public String getModerationStatus() { return moderationStatus; }
    public void setModerationStatus(String moderationStatus) { this.moderationStatus = moderationStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public UserInfo getUserInfo() { return userInfo; }
    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }

    public RestaurantInfo getRestaurantInfo() { return restaurantInfo; }
    public void setRestaurantInfo(RestaurantInfo restaurantInfo) { this.restaurantInfo = restaurantInfo; }
}