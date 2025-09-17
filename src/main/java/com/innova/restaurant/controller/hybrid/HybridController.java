package com.innova.restaurant.controller.hybrid;

import com.innova.restaurant.model.document.ReviewDocument;
import com.innova.restaurant.model.document.UserActivityDocument;
import com.innova.restaurant.service.hybrid.HybridReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador que demuestra la arquitectura híbrida PostgreSQL + MongoDB
 * Sin uso de @Query - solo query methods automáticos
 */
@RestController
@RequestMapping("/api/hybrid")
public class HybridController {

    @Autowired
    private HybridReviewService hybridReviewService;

    /**
     * Crear una nueva review - Demuestra coordinación entre PostgreSQL y MongoDB
     */
    @PostMapping("/reviews")
    public ResponseEntity<ReviewDocument> createReview(@RequestBody CreateReviewRequest request) {
        
        ReviewDocument review = hybridReviewService.createReview(
            request.getUserId(),
            request.getRestaurantId(), 
            request.getOverallRating(),
            request.getComment(),
            request.getDetailedRatings()
        );
        
        return ResponseEntity.ok(review);
    }

    /**
     * Obtener reviews de un restaurante con paginación
     */
    @GetMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<Page<ReviewDocument>> getRestaurantReviews(
            @PathVariable Long restaurantId,
            Pageable pageable) {
        
        Page<ReviewDocument> reviews = hybridReviewService.getRestaurantReviews(restaurantId, pageable);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Obtener reviews de un usuario específico
     */
    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<List<ReviewDocument>> getUserReviews(@PathVariable Long userId) {
        
        List<ReviewDocument> reviews = hybridReviewService.getUserReviews(userId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Responder a una review
     */
    @PostMapping("/reviews/{reviewId}/respond")
    public ResponseEntity<ReviewDocument> respondToReview(
            @PathVariable String reviewId,
            @RequestBody RespondToReviewRequest request) {
        
        ReviewDocument review = hybridReviewService.respondToReview(
            reviewId, 
            request.getResponse(), 
            request.getRestaurantOwnerId()
        );
        
        return ResponseEntity.ok(review);
    }

    /**
     * Buscar reviews por keyword - Demuestra query methods sin @Query
     */
    @GetMapping("/reviews/search")
    public ResponseEntity<List<ReviewDocument>> searchReviews(@RequestParam String keyword) {
        
        List<ReviewDocument> reviews = hybridReviewService.searchReviewsByKeyword(keyword);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Obtener estadísticas híbridas de un restaurante
     */
    @GetMapping("/restaurants/{restaurantId}/statistics")
    public ResponseEntity<Map<String, Object>> getRestaurantStatistics(@PathVariable Long restaurantId) {
        
        Map<String, Object> statistics = hybridReviewService.getRestaurantStatistics(restaurantId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Obtener reviews recientes de un restaurante
     */
    @GetMapping("/restaurants/{restaurantId}/reviews/recent")
    public ResponseEntity<List<ReviewDocument>> getRecentReviews(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<ReviewDocument> reviews = hybridReviewService.getRecentReviews(restaurantId, limit);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Obtener reviews por rango de fechas
     */
    @GetMapping("/restaurants/{restaurantId}/reviews/daterange")
    public ResponseEntity<List<ReviewDocument>> getReviewsByDateRange(
            @PathVariable Long restaurantId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        
        List<ReviewDocument> reviews = hybridReviewService.getReviewsByDateRange(restaurantId, start, end);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Obtener actividad de un usuario
     */
    @GetMapping("/users/{userId}/activity")
    public ResponseEntity<List<UserActivityDocument>> getUserActivity(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "20") int limit) {
        
        List<UserActivityDocument> activities = hybridReviewService.getUserActivity(userId, limit);
        return ResponseEntity.ok(activities);
    }

    /**
     * Endpoint de demostración que muestra la capacidad híbrida completa
     */
    @GetMapping("/demo/hybrid-capabilities")
    public ResponseEntity<Map<String, Object>> demonstrateHybridCapabilities() {
        
        Map<String, Object> capabilities = Map.of(
            "message", "Arquitectura Híbrida PostgreSQL + MongoDB",
            "postgresql_features", List.of(
                "Relaciones ACID entre User, Restaurant, Reservation",
                "Query methods automáticos sin @Query",
                "JPA Specifications para queries dinámicos",
                "Integridad referencial garantizada"
            ),
            "mongodb_features", List.of(
                "Documentos flexibles para Reviews, Analytics, Logs",
                "Query methods sin @Query para MongoDB",
                "Desnormalización controlada para performance",
                "Escalabilidad horizontal para big data"
            ),
            "hybrid_coordination", List.of(
                "Validación en PostgreSQL, almacenamiento en MongoDB",
                "Logs de actividad automáticos en MongoDB",
                "Estadísticas calculadas desde ambas fuentes",
                "Servicios coordinados con transacciones mixtas"
            ),
            "query_methods_demo", List.of(
                "findByRestaurantId() - MongoDB sin @Query",
                "findByFirstNameContaining() - PostgreSQL sin @Query", 
                "findByOverallRatingGreaterThan() - MongoDB automático",
                "findByEmailAndFirstName() - PostgreSQL automático"
            )
        );
        
        return ResponseEntity.ok(capabilities);
    }

    // DTOs para requests
    public static class CreateReviewRequest {
        private Long userId;
        private Long restaurantId;
        private Double overallRating;
        private String comment;
        private Map<String, Double> detailedRatings;

        // Getters y Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getRestaurantId() { return restaurantId; }
        public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

        public Double getOverallRating() { return overallRating; }
        public void setOverallRating(Double overallRating) { this.overallRating = overallRating; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }

        public Map<String, Double> getDetailedRatings() { return detailedRatings; }
        public void setDetailedRatings(Map<String, Double> detailedRatings) { this.detailedRatings = detailedRatings; }
    }

    public static class RespondToReviewRequest {
        private String response;
        private Long restaurantOwnerId;

        // Getters y Setters
        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }

        public Long getRestaurantOwnerId() { return restaurantOwnerId; }
        public void setRestaurantOwnerId(Long restaurantOwnerId) { this.restaurantOwnerId = restaurantOwnerId; }
    }
}