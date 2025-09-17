package com.innova.restaurant.repository.document;

import com.innova.restaurant.model.document.ReviewDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para ReviewDocument utilizando query methods sin @Query
 */
@Repository
public interface ReviewDocumentRepository extends MongoRepository<ReviewDocument, String> {

    // Query methods sin @Query - demostración de funcionalidad híbrida

    // Buscar por restaurante
    List<ReviewDocument> findByRestaurantId(Long restaurantId);
    Page<ReviewDocument> findByRestaurantId(Long restaurantId, Pageable pageable);

    // Buscar por usuario
    List<ReviewDocument> findByUserId(Long userId);
    Page<ReviewDocument> findByUserId(Long userId, Pageable pageable);

    // Buscar por rating
    List<ReviewDocument> findByOverallRatingGreaterThanEqual(Double minRating);
    List<ReviewDocument> findByOverallRatingBetween(Double minRating, Double maxRating);

    // Buscar por estado de moderación
    List<ReviewDocument> findByModerationStatus(String moderationStatus);
    Page<ReviewDocument> findByModerationStatus(String moderationStatus, Pageable pageable);

    // Buscar reviews verificadas
    List<ReviewDocument> findByIsVerifiedTrue();
    List<ReviewDocument> findByIsVerifiedFalse();

    // Buscar por fecha
    List<ReviewDocument> findByCreatedAtAfter(LocalDateTime date);
    List<ReviewDocument> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar por restaurante y fecha
    List<ReviewDocument> findByRestaurantIdAndCreatedAtBetween(Long restaurantId, LocalDateTime startDate, LocalDateTime endDate);

    // Buscar reviews con respuesta del restaurante
    List<ReviewDocument> findByRestaurantResponseIsNotNull();
    List<ReviewDocument> findByRestaurantResponseIsNull();

    // Queries complejas sin @Query
    List<ReviewDocument> findByRestaurantIdAndOverallRatingGreaterThanEqual(Long restaurantId, Double minRating);
    List<ReviewDocument> findByRestaurantIdAndModerationStatus(Long restaurantId, String moderationStatus);
    List<ReviewDocument> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime date);

    // Buscar por palabras clave en comentarios (usando regex automático)
    List<ReviewDocument> findByCommentContainingIgnoreCase(String keyword);
    List<ReviewDocument> findByRestaurantIdAndCommentContainingIgnoreCase(Long restaurantId, String keyword);

    // Buscar reviews recientes de un restaurante
    List<ReviewDocument> findTop10ByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
    List<ReviewDocument> findTop5ByRestaurantIdAndOverallRatingGreaterThanEqualOrderByCreatedAtDesc(
        Long restaurantId, Double minRating);

    // Contar reviews
    long countByRestaurantId(Long restaurantId);
    long countByRestaurantIdAndOverallRatingGreaterThanEqual(Long restaurantId, Double minRating);
    long countByModerationStatus(String moderationStatus);

    // Buscar reviews que necesitan moderación
    List<ReviewDocument> findByModerationStatusAndCreatedAtBefore(String moderationStatus, LocalDateTime date);

    // Buscar por tags
    List<ReviewDocument> findByTagsContaining(String tag);
    List<ReviewDocument> findByRestaurantIdAndTagsContaining(Long restaurantId, String tag);

    // Solo para demostración - algunos ejemplos con @Query si fuera necesario
    // Pero preferimos usar query methods cuando es posible

    @Query("{ 'restaurantId': ?0, 'overallRating': { $gte: ?1 }, 'createdAt': { $gte: ?2 } }")
    List<ReviewDocument> findRecentHighRatedReviews(Long restaurantId, Double minRating, LocalDateTime since);

    @Query("{ 'restaurantId': ?0, 'detailedRatings.food': { $gte: ?1 } }")
    List<ReviewDocument> findByRestaurantAndFoodRating(Long restaurantId, Double minFoodRating);

    // Aggregation example (estas sí requieren @Query por ser agregaciones complejas)
    @Query(value = "{ 'restaurantId': ?0 }", fields = "{ 'overallRating': 1, 'createdAt': 1 }")
    List<ReviewDocument> findRatingsOnlyByRestaurant(Long restaurantId);
}