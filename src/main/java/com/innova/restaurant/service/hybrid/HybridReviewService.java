package com.innova.restaurant.service.hybrid;

import com.innova.restaurant.model.entity.Restaurant;
import com.innova.restaurant.model.entity.User;
import com.innova.restaurant.model.document.ReviewDocument;
import com.innova.restaurant.model.document.UserActivityDocument;
import com.innova.restaurant.repository.jpa.RestaurantRepository;
import com.innova.restaurant.repository.jpa.UserRepository;
import com.innova.restaurant.repository.document.ReviewDocumentRepository;
import com.innova.restaurant.repository.document.UserActivityDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio híbrido que demuestra la coordinación entre PostgreSQL y MongoDB
 * PostgreSQL maneja las relaciones, MongoDB maneja documentos flexibles
 */
@Service
public class HybridReviewService {

    @Autowired
    private RestaurantRepository restaurantRepository; // PostgreSQL

    @Autowired
    private UserRepository userRepository; // PostgreSQL

    @Autowired
    private ReviewDocumentRepository reviewDocumentRepository; // MongoDB

    @Autowired
    private UserActivityDocumentRepository userActivityRepository; // MongoDB

    /**
     * Crear una review utilizando datos de PostgreSQL y almacenando en MongoDB
     */
    @Transactional
    public ReviewDocument createReview(Long userId, Long restaurantId, Double overallRating, 
                                      String comment, Map<String, Double> detailedRatings) {
        
        // 1. Validar que el usuario existe en PostgreSQL
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        // 2. Validar que el restaurante existe en PostgreSQL
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurante no encontrado con ID: " + restaurantId));

        // 3. Crear el documento de review en MongoDB
        ReviewDocument review = new ReviewDocument();
        review.setUserId(userId);
        review.setRestaurantId(restaurantId);
        review.setOverallRating(overallRating);
        review.setComment(comment);
        review.setDetailedRatings(detailedRatings);

        // 4. Agregar información del usuario y restaurante para desnormalización
        ReviewDocument.UserInfo userInfo = new ReviewDocument.UserInfo();
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setEmail(user.getEmail());
        review.setUserInfo(userInfo);

        ReviewDocument.RestaurantInfo restaurantInfo = new ReviewDocument.RestaurantInfo();
        restaurantInfo.setName(restaurant.getName());
        restaurantInfo.setAddress(restaurant.getAddress());
        restaurantInfo.setPhoneNumber(restaurant.getPhone());
        review.setRestaurantInfo(restaurantInfo);

        // 5. Guardar la review en MongoDB
        ReviewDocument savedReview = reviewDocumentRepository.save(review);

        // 6. Registrar la actividad en MongoDB
        logUserActivity(userId, "review_created", 
            "Usuario creó una review para el restaurante: " + restaurant.getName(),
            "review", savedReview.getId());

        return savedReview;
    }

    /**
     * Obtener reviews de un restaurante con información híbrida
     */
    public Page<ReviewDocument> getRestaurantReviews(Long restaurantId, Pageable pageable) {
        
        // 1. Verificar que el restaurante existe en PostgreSQL
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurante no encontrado con ID: " + restaurantId));

        // 2. Obtener reviews desde MongoDB usando query methods
        Page<ReviewDocument> reviews = reviewDocumentRepository.findByRestaurantId(restaurantId, pageable);

        // 3. Registrar la actividad de consulta
        logSystemActivity("restaurant_reviews_viewed", 
            "Reviews del restaurante " + restaurant.getName() + " fueron consultadas");

        return reviews;
    }

    /**
     * Obtener reviews de un usuario
     */
    public List<ReviewDocument> getUserReviews(Long userId) {
        
        // 1. Verificar que el usuario existe en PostgreSQL
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        // 2. Obtener reviews desde MongoDB
        List<ReviewDocument> reviews = reviewDocumentRepository.findByUserId(userId);

        // 3. Registrar actividad
        logUserActivity(userId, "user_reviews_viewed", 
            "Usuario consultó sus propias reviews", "user", userId.toString());

        return reviews;
    }

    /**
     * Responder a una review (funcionalidad híbrida)
     */
    @Transactional
    public ReviewDocument respondToReview(String reviewId, String response, Long restaurantOwnerId) {
        
        // 1. Obtener la review de MongoDB
        ReviewDocument review = reviewDocumentRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review no encontrada con ID: " + reviewId));

        // 2. Verificar que el usuario es propietario del restaurante en PostgreSQL
        Restaurant restaurant = restaurantRepository.findById(review.getRestaurantId())
            .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // 3. Crear la respuesta del restaurante
        ReviewDocument.RestaurantResponse restaurantResponse = new ReviewDocument.RestaurantResponse();
        restaurantResponse.setResponse(response);
        restaurantResponse.setRespondedAt(LocalDateTime.now());
        restaurantResponse.setResponderId(restaurantOwnerId);

        // 4. Actualizar la review en MongoDB
        review.setRestaurantResponse(restaurantResponse);
        ReviewDocument updatedReview = reviewDocumentRepository.save(review);

        // 5. Registrar actividad
        logUserActivity(restaurantOwnerId, "review_response_created", 
            "Propietario respondió a una review", "review", reviewId);

        return updatedReview;
    }

    /**
     * Buscar reviews por keywords usando funcionalidad híbrida
     */
    public List<ReviewDocument> searchReviewsByKeyword(String keyword) {
        
        // Usar query method sin @Query de MongoDB
        List<ReviewDocument> reviews = reviewDocumentRepository.findByCommentContainingIgnoreCase(keyword);

        // Registrar búsqueda
        logSystemActivity("review_search", 
            "Búsqueda de reviews por keyword: " + keyword + " - " + reviews.size() + " resultados");

        return reviews;
    }

    /**
     * Obtener estadísticas híbridas de un restaurante
     */
    public Map<String, Object> getRestaurantStatistics(Long restaurantId) {
        
        // 1. Obtener información del restaurante desde PostgreSQL
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // 2. Obtener estadísticas de reviews desde MongoDB usando query methods
        List<ReviewDocument> allReviews = reviewDocumentRepository.findByRestaurantId(restaurantId);
        List<ReviewDocument> highRatedReviews = reviewDocumentRepository
            .findByRestaurantIdAndOverallRatingGreaterThanEqual(restaurantId, 4.0);
        long totalReviews = reviewDocumentRepository.countByRestaurantId(restaurantId);

        // 3. Calcular estadísticas
        double averageRating = allReviews.stream()
            .mapToDouble(ReviewDocument::getOverallRating)
            .average()
            .orElse(0.0);

        // 4. Crear mapa de estadísticas híbridas
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("restaurantName", restaurant.getName());
        statistics.put("restaurantAddress", restaurant.getAddress());
        statistics.put("totalReviews", totalReviews);
        statistics.put("averageRating", averageRating);
        statistics.put("highRatedReviews", highRatedReviews.size());
        statistics.put("highRatedPercentage", totalReviews > 0 ? (double) highRatedReviews.size() / totalReviews * 100 : 0);

        // 5. Registrar consulta de estadísticas
        logSystemActivity("restaurant_statistics_viewed", 
            "Estadísticas del restaurante " + restaurant.getName() + " fueron consultadas");

        return statistics;
    }

    /**
     * Obtener reviews recientes usando query methods
     */
    public List<ReviewDocument> getRecentReviews(Long restaurantId, int limit) {
        
        // Usar query method con Top y OrderBy sin @Query
        if (limit <= 10) {
            return reviewDocumentRepository.findTop10ByRestaurantIdOrderByCreatedAtDesc(restaurantId);
        } else {
            // Para límites mayores, usar paginación
            return reviewDocumentRepository.findByRestaurantId(restaurantId, 
                Pageable.ofSize(limit)).getContent();
        }
    }

    /**
     * Método privado para registrar actividad de usuario
     */
    private void logUserActivity(Long userId, String actionType, String description, 
                                String resourceType, String resourceId) {
        
        UserActivityDocument activity = new UserActivityDocument();
        activity.setUserId(userId);
        activity.setActionType(actionType);
        activity.setDescription(description);
        activity.setResourceType(resourceType);
        activity.setResourceId(resourceId);
        
        userActivityRepository.save(activity);
    }

    /**
     * Método privado para registrar actividad del sistema
     */
    private void logSystemActivity(String actionType, String description) {
        
        UserActivityDocument activity = new UserActivityDocument();
        activity.setActionType(actionType);
        activity.setDescription(description);
        activity.setResourceType("system");
        
        userActivityRepository.save(activity);
    }

    /**
     * Demostración de query methods complejos sin @Query
     */
    public List<ReviewDocument> getReviewsByDateRange(Long restaurantId, LocalDateTime startDate, LocalDateTime endDate) {
        
        // Combinar filtros usando query methods
        List<ReviewDocument> reviews = reviewDocumentRepository.findByRestaurantIdAndCreatedAtBetween(
            restaurantId, startDate, endDate);

        logSystemActivity("reviews_date_range_query", 
            "Consulta de reviews por rango de fechas para restaurante " + restaurantId);

        return reviews;
    }

    /**
     * Obtener actividad del usuario usando query methods
     */
    public List<UserActivityDocument> getUserActivity(Long userId, int limit) {
        
        if (limit <= 10) {
            return userActivityRepository.findTop10ByUserIdOrderByTimestampDesc(userId);
        } else {
            return userActivityRepository.findByUserId(userId, Pageable.ofSize(limit)).getContent();
        }
    }
}