package com.innova.restaurant.controller;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innova.restaurant.dto.RestaurantWithReviewsDto;
import com.innova.restaurant.model.entity.Restaurant;
import com.innova.restaurant.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para gestión de restaurantes
 * Cumple con el modelo de madurez Richardson Nivel 2
 * Incluye integración con reviews de MongoDB
 */
@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurants", description = "Gestión de restaurantes con reviews integrados")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    /**
     * GET /api/restaurants - Obtener todos los restaurantes con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Restaurant>> getAllRestaurants(Pageable pageable) {
        Page<Restaurant> restaurants = restaurantService.findAllRestaurants(pageable);
        return ResponseEntity.ok(restaurants);
    }

    /**
     * GET /api/restaurants/{id} - Obtener restaurante por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    /**
     * POST /api/v1/restaurants - Crear nuevo restaurante
     */
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantService.createRestaurant(request);
        URI location = URI.create("/api/v1/restaurants/" + restaurant.getId());
        return ResponseEntity.created(location).body(restaurant);
    }

    /**
     * PUT /api/v1/restaurants/{id} - Actualizar restaurante completo
     */
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateRestaurantRequest request) {
        Restaurant restaurant = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.ok(restaurant);
    }

    /**
     * PATCH /api/v1/restaurants/{id} - Actualización parcial
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Restaurant> patchRestaurant(
            @PathVariable Long id, 
            @RequestBody PatchRestaurantRequest request) {
        Restaurant restaurant = restaurantService.patchRestaurant(id, request);
        return ResponseEntity.ok(restaurant);
    }

    /**
     * DELETE /api/v1/restaurants/{id} - Eliminar restaurante (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/restaurants/search - Buscar restaurantes por nombre
     */
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(
            @RequestParam String name,
            @RequestParam(defaultValue = "true") boolean activeOnly) {
        List<Restaurant> restaurants = restaurantService.searchByName(name, activeOnly);
        return ResponseEntity.ok(restaurants);
    }

    /**
     * GET /api/v1/restaurants/by-owner/{ownerId} - Restaurantes por propietario
     */
    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByOwner(@PathVariable Long ownerId) {
        List<Restaurant> restaurants = restaurantService.findByOwner(ownerId);
        return ResponseEntity.ok(restaurants);
    }

    /**
     * PUT /api/v1/restaurants/{id}/status - Activar/desactivar restaurante
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(
            @PathVariable Long id, 
            @RequestParam boolean active) {
        Restaurant restaurant = restaurantService.updateStatus(id, active);
        return ResponseEntity.ok(restaurant);
    }

    // ===== NUEVOS ENDPOINTS CON REVIEWS INTEGRADOS =====

    /**
     * GET /api/restaurants/with-reviews - Obtener todos los restaurantes con sus reviews
     */
    @GetMapping("/with-reviews")
    @Operation(summary = "Obtener restaurantes con reviews", 
               description = "Obtiene todos los restaurantes con sus reviews de MongoDB integrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurantes obtenidos exitosamente")
    })
    public ResponseEntity<Page<RestaurantWithReviewsDto>> getAllRestaurantsWithReviews(
            @Parameter(description = "Paginación para restaurantes") Pageable restaurantsPageable,
            @RequestParam(defaultValue = "0") @Parameter(description = "Página de reviews") int reviewsPage,
            @RequestParam(defaultValue = "5") @Parameter(description = "Tamaño de página de reviews") int reviewsSize) {
        
        Pageable reviewsPageable = PageRequest.of(reviewsPage, reviewsSize);
        Page<RestaurantWithReviewsDto> restaurants = restaurantService.findAllRestaurantsWithReviews(
            restaurantsPageable, reviewsPageable);
        return ResponseEntity.ok(restaurants);
    }

    /**
     * GET /api/restaurants/{id}/with-reviews - Obtener restaurante específico con reviews paginados
     */
    @GetMapping("/{id}/with-reviews")
    @Operation(summary = "Obtener restaurante con reviews paginados", 
               description = "Obtiene un restaurante específico con sus reviews de MongoDB paginados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Restaurante no encontrado")
    })
    public ResponseEntity<RestaurantWithReviewsDto> getRestaurantWithReviews(
            @PathVariable @Parameter(description = "ID del restaurante") Long id,
            @Parameter(description = "Paginación para reviews") Pageable reviewsPageable) {
        
        RestaurantWithReviewsDto restaurant = restaurantService.findRestaurantWithReviews(id, reviewsPageable);
        return ResponseEntity.ok(restaurant);
    }

    /**
     * GET /api/restaurants/{id}/with-recent-reviews - Obtener restaurante con reviews recientes
     */
    @GetMapping("/{id}/with-recent-reviews")
    @Operation(summary = "Obtener restaurante con reviews recientes", 
               description = "Obtiene un restaurante con sus reviews más recientes (sin paginación)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Restaurante no encontrado")
    })
    public ResponseEntity<RestaurantWithReviewsDto> getRestaurantWithRecentReviews(
            @PathVariable @Parameter(description = "ID del restaurante") Long id,
            @RequestParam(defaultValue = "10") @Parameter(description = "Número de reviews recientes") int limit) {
        
        RestaurantWithReviewsDto restaurant = restaurantService.findRestaurantWithRecentReviews(id, limit);
        return ResponseEntity.ok(restaurant);
    }

    // DTOs para requests
    public static class CreateRestaurantRequest {
        private String name;
        private String description;
        private String address;
        private String phone;
        private String email;
        private LocalTime openingTime;
        private LocalTime closingTime;
        private Integer maxCapacity;
        private Long ownerId;

        // Getters y Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public LocalTime getOpeningTime() { return openingTime; }
        public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }

        public LocalTime getClosingTime() { return closingTime; }
        public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }

        public Integer getMaxCapacity() { return maxCapacity; }
        public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }

        public Long getOwnerId() { return ownerId; }
        public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    }

    public static class UpdateRestaurantRequest {
        private String name;
        private String description;
        private String address;
        private String phone;
        private String email;
        private LocalTime openingTime;
        private LocalTime closingTime;
        private Integer maxCapacity;

        // Getters y Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public LocalTime getOpeningTime() { return openingTime; }
        public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }

        public LocalTime getClosingTime() { return closingTime; }
        public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }

        public Integer getMaxCapacity() { return maxCapacity; }
        public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
    }

    public static class PatchRestaurantRequest {
        private String name;
        private String description;
        private String address;
        private String phone;
        private String email;
        private LocalTime openingTime;
        private LocalTime closingTime;
        private Integer maxCapacity;
        private Boolean isActive;

        // Getters y Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public LocalTime getOpeningTime() { return openingTime; }
        public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }

        public LocalTime getClosingTime() { return closingTime; }
        public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }

        public Integer getMaxCapacity() { return maxCapacity; }
        public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }

        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    }
}