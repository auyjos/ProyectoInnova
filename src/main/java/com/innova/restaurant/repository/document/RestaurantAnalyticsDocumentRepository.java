package com.innova.restaurant.repository.document;

import com.innova.restaurant.model.document.RestaurantAnalyticsDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para RestaurantAnalyticsDocument utilizando query methods sin @Query
 */
@Repository
public interface RestaurantAnalyticsDocumentRepository extends MongoRepository<RestaurantAnalyticsDocument, String> {

    // Query methods sin @Query - funcionalidad híbrida completa

    // Buscar por restaurante
    List<RestaurantAnalyticsDocument> findByRestaurantId(Long restaurantId);
    Page<RestaurantAnalyticsDocument> findByRestaurantId(Long restaurantId, Pageable pageable);

    // Buscar por tipo de período
    List<RestaurantAnalyticsDocument> findByPeriodType(String periodType);
    Page<RestaurantAnalyticsDocument> findByPeriodType(String periodType, Pageable pageable);

    // Buscar por restaurante y tipo de período
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodType(Long restaurantId, String periodType);
    Page<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodType(Long restaurantId, String periodType, Pageable pageable);

    // Buscar por rango de fechas
    List<RestaurantAnalyticsDocument> findByPeriodStartAfter(LocalDateTime date);
    List<RestaurantAnalyticsDocument> findByPeriodEndBefore(LocalDateTime date);
    List<RestaurantAnalyticsDocument> findByPeriodStartBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<RestaurantAnalyticsDocument> findByPeriodEndBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar por período específico
    List<RestaurantAnalyticsDocument> findByPeriodStartAfterAndPeriodEndBefore(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar analytics recientes
    List<RestaurantAnalyticsDocument> findByLastUpdatedAfter(LocalDateTime date);
    List<RestaurantAnalyticsDocument> findByCalculatedAtAfter(LocalDateTime date);

    // Queries complejas por restaurante y fechas
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodStartAfter(Long restaurantId, LocalDateTime date);
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodEndBefore(Long restaurantId, LocalDateTime date);
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodStartBetween(Long restaurantId, LocalDateTime startDate, LocalDateTime endDate);

    // Buscar por restaurante, tipo y fechas
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodTypeAndPeriodStartAfter(
        Long restaurantId, String periodType, LocalDateTime date);
    
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodTypeAndPeriodStartBetween(
        Long restaurantId, String periodType, LocalDateTime startDate, LocalDateTime endDate);

    // Encontrar el analytics más reciente de un restaurante por tipo
    Optional<RestaurantAnalyticsDocument> findTopByRestaurantIdAndPeriodTypeOrderByPeriodStartDesc(
        Long restaurantId, String periodType);

    // Encontrar analytics más recientes de un restaurante
    List<RestaurantAnalyticsDocument> findTop5ByRestaurantIdOrderByPeriodStartDesc(Long restaurantId);
    List<RestaurantAnalyticsDocument> findTop10ByRestaurantIdAndPeriodTypeOrderByPeriodStartDesc(
        Long restaurantId, String periodType);

    // Ordenar por fechas
    List<RestaurantAnalyticsDocument> findByRestaurantIdOrderByPeriodStartDesc(Long restaurantId);
    List<RestaurantAnalyticsDocument> findByRestaurantIdOrderByPeriodStartAsc(Long restaurantId);
    List<RestaurantAnalyticsDocument> findByPeriodTypeOrderByPeriodStartDesc(String periodType);

    // Contar registros
    long countByRestaurantId(Long restaurantId);
    long countByPeriodType(String periodType);
    long countByRestaurantIdAndPeriodType(Long restaurantId, String periodType);
    long countByPeriodStartAfter(LocalDateTime date);
    long countByRestaurantIdAndPeriodStartAfter(Long restaurantId, LocalDateTime date);

    // Buscar por última actualización
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndLastUpdatedAfter(Long restaurantId, LocalDateTime date);
    Page<RestaurantAnalyticsDocument> findByLastUpdatedAfterOrderByLastUpdatedDesc(LocalDateTime date, Pageable pageable);

    // Verificar existencia de analytics específicos
    boolean existsByRestaurantIdAndPeriodTypeAndPeriodStartAndPeriodEnd(
        Long restaurantId, String periodType, LocalDateTime periodStart, LocalDateTime periodEnd);

    // Buscar analytics calculados recientemente
    List<RestaurantAnalyticsDocument> findByCalculatedAtAfterOrderByCalculatedAtDesc(LocalDateTime date);
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndCalculatedAtAfter(Long restaurantId, LocalDateTime date);

    // Buscar por tipo de período y fecha de cálculo
    List<RestaurantAnalyticsDocument> findByPeriodTypeAndCalculatedAtAfter(String periodType, LocalDateTime date);

    // Analytics que necesitan recálculo (ejemplo: más antiguos que X tiempo)
    List<RestaurantAnalyticsDocument> findByLastUpdatedBefore(LocalDateTime date);
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndLastUpdatedBefore(Long restaurantId, LocalDateTime date);

    // Buscar el período más reciente de cada tipo para un restaurante
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodTypeOrderByPeriodStartDesc(
        Long restaurantId, String periodType);

    // Verificación de datos específicos por período
    Optional<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodTypeAndPeriodStart(
        Long restaurantId, String periodType, LocalDateTime periodStart);

    // Búsqueda para comparaciones temporales
    List<RestaurantAnalyticsDocument> findByRestaurantIdAndPeriodTypeAndPeriodStartAfterOrderByPeriodStartAsc(
        Long restaurantId, String periodType, LocalDateTime afterDate);

    // Analytics por múltiples restaurantes (para comparaciones)
    List<RestaurantAnalyticsDocument> findByRestaurantIdInAndPeriodType(List<Long> restaurantIds, String periodType);
    List<RestaurantAnalyticsDocument> findByRestaurantIdInAndPeriodTypeAndPeriodStartBetween(
        List<Long> restaurantIds, String periodType, LocalDateTime startDate, LocalDateTime endDate);
}