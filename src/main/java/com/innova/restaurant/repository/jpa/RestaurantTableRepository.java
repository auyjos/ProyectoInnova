package com.innova.restaurant.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.innova.restaurant.model.entity.RestaurantTable;
import com.innova.restaurant.model.enums.TableStatus;

/**
 * Repositorio JPA para la entidad RestaurantTable
 * Utiliza métodos automáticos de Spring Data JPA siguiendo el patrón establecido
 * sin usar anotaciones @Query personalizadas.
 */
@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long>, JpaSpecificationExecutor<RestaurantTable> {

    /**
     * Busca mesas por ID de restaurante
     *
     * @param restaurantId ID del restaurante
     * @return lista de mesas del restaurante
     */
    List<RestaurantTable> findByRestaurantId(Long restaurantId);

    /**
     * Busca mesas por estado en un restaurante
     *
     * @param restaurantId ID del restaurante
     * @param status estado de la mesa
     * @return lista de mesas con el estado especificado
     */
    List<RestaurantTable> findByRestaurantIdAndStatus(Long restaurantId, TableStatus status);

    /**
     * Busca mesas por capacidad mínima
     *
     * @param capacity capacidad mínima requerida
     * @return lista de mesas con capacidad suficiente
     */
    List<RestaurantTable> findByCapacityGreaterThanEqual(Integer capacity);

    /**
     * Busca mesas por restaurante y capacidad
     *
     * @param restaurantId ID del restaurante
     * @param capacity capacidad mínima requerida
     * @return lista de mesas del restaurante con capacidad suficiente
     */
    List<RestaurantTable> findByRestaurantIdAndCapacityGreaterThanEqual(Long restaurantId, Integer capacity);

    /**
     * Busca mesas por número de mesa en un restaurante
     *
     * @param restaurantId ID del restaurante
     * @param tableNumber número de mesa
     * @return mesa con el número especificado
     */
    RestaurantTable findByRestaurantIdAndTableNumber(Long restaurantId, Integer tableNumber);

    /**
     * Cuenta el número total de mesas en un restaurante
     *
     * @param restaurantId ID del restaurante
     * @return número total de mesas
     */
    long countByRestaurantId(Long restaurantId);

    /**
     * Cuenta mesas por estado en un restaurante
     *
     * @param restaurantId ID del restaurante
     * @param status estado de la mesa
     * @return número de mesas con el estado especificado
     */
    long countByRestaurantIdAndStatus(Long restaurantId, TableStatus status);
}