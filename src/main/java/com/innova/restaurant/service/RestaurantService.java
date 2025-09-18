package com.innova.restaurant.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.innova.restaurant.controller.RestaurantController;
import com.innova.restaurant.dto.RestaurantWithReviewsDto;
import com.innova.restaurant.model.entity.Restaurant;

/**
 * Interfaz del servicio de restaurantes
 */
public interface RestaurantService {

    /**
     * Encuentra todos los restaurantes con paginación
     */
    Page<Restaurant> findAllRestaurants(Pageable pageable);

    /**
     * Encuentra un restaurante por ID
     */
    Restaurant findRestaurantById(Long id);

    /**
     * Crea un nuevo restaurante
     */
    Restaurant createRestaurant(RestaurantController.CreateRestaurantRequest request);

    /**
     * Actualiza un restaurante completo
     */
    Restaurant updateRestaurant(Long id, RestaurantController.UpdateRestaurantRequest request);

    /**
     * Actualización parcial de un restaurante
     */
    Restaurant patchRestaurant(Long id, RestaurantController.PatchRestaurantRequest request);

    /**
     * Elimina un restaurante (soft delete)
     */
    void deleteRestaurant(Long id);

    /**
     * Busca restaurantes por nombre
     */
    List<Restaurant> searchByName(String name, boolean activeOnly);

    /**
     * Encuentra restaurantes por propietario
     */
    List<Restaurant> findByOwner(Long ownerId);

    /**
     * Actualiza el estado activo/inactivo de un restaurante
     */
    Restaurant updateStatus(Long id, boolean active);

    /**
     * Encuentra un restaurante por ID con sus reviews integrados
     */
    RestaurantWithReviewsDto findRestaurantWithReviews(Long id, Pageable reviewsPageable);

    /**
     * Encuentra todos los restaurantes con sus reviews integrados
     */
    Page<RestaurantWithReviewsDto> findAllRestaurantsWithReviews(Pageable restaurantsPageable, Pageable reviewsPageable);

    /**
     * Encuentra un restaurante con sus reviews más recientes (sin paginación)
     */
    RestaurantWithReviewsDto findRestaurantWithRecentReviews(Long id, int reviewLimit);
}