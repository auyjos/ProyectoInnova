package com.innova.restaurant.service;

import java.util.List;

import com.innova.restaurant.controller.RestaurantTableController.CreateTableRequest;
import com.innova.restaurant.controller.RestaurantTableController.UpdateTableRequest;
import com.innova.restaurant.model.entity.RestaurantTable;

/**
 * Servicio para gestión de mesas de restaurantes
 */
public interface RestaurantTableService {

    /**
     * Buscar todas las mesas de un restaurante
     */
    List<RestaurantTable> findByRestaurantId(Long restaurantId);

    /**
     * Buscar mesa por ID (validando que pertenezca al restaurante)
     */
    RestaurantTable findTableById(Long tableId, Long restaurantId);

    /**
     * Crear nueva mesa para un restaurante
     */
    RestaurantTable createTable(Long restaurantId, CreateTableRequest request);

    /**
     * Actualizar mesa existente
     */
    RestaurantTable updateTable(Long tableId, Long restaurantId, UpdateTableRequest request);

    /**
     * Eliminar mesa
     */
    void deleteTable(Long tableId, Long restaurantId);

    /**
     * Buscar mesas disponibles de un restaurante
     */
    List<RestaurantTable> findAvailableTablesByRestaurant(Long restaurantId);

    /**
     * Verificar si una mesa existe y pertenece al restaurante
     */
    boolean existsByIdAndRestaurantId(Long tableId, Long restaurantId);
}