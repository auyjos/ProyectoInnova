package com.innova.restaurant.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innova.restaurant.model.entity.RestaurantTable;
import com.innova.restaurant.model.enums.TableStatus;
import com.innova.restaurant.service.RestaurantTableService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Controlador REST para gestión de mesas de restaurantes
 * Permite a los propietarios gestionar las mesas de sus restaurantes
 */
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/tables")
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService restaurantTableService;

    /**
     * GET /api/restaurants/{restaurantId}/tables - Obtener todas las mesas de un restaurante
     */
    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getTablesByRestaurant(@PathVariable Long restaurantId) {
        List<RestaurantTable> tables = restaurantTableService.findByRestaurantId(restaurantId);
        return ResponseEntity.ok(tables);
    }

    /**
     * GET /api/restaurants/{restaurantId}/tables/{tableId} - Obtener mesa específica
     */
    @GetMapping("/{tableId}")
    public ResponseEntity<RestaurantTable> getTableById(
            @PathVariable Long restaurantId, 
            @PathVariable Long tableId) {
        RestaurantTable table = restaurantTableService.findTableById(tableId, restaurantId);
        return ResponseEntity.ok(table);
    }

    /**
     * POST /api/restaurants/{restaurantId}/tables - Crear nueva mesa
     */
    @PostMapping
    public ResponseEntity<RestaurantTable> createTable(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateTableRequest request) {
        RestaurantTable table = restaurantTableService.createTable(restaurantId, request);
        URI location = URI.create("/api/restaurants/" + restaurantId + "/tables/" + table.getId());
        return ResponseEntity.created(location).body(table);
    }

    /**
     * PUT /api/restaurants/{restaurantId}/tables/{tableId} - Actualizar mesa
     */
    @PutMapping("/{tableId}")
    public ResponseEntity<RestaurantTable> updateTable(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId,
            @Valid @RequestBody UpdateTableRequest request) {
        RestaurantTable table = restaurantTableService.updateTable(tableId, restaurantId, request);
        return ResponseEntity.ok(table);
    }

    /**
     * DELETE /api/restaurants/{restaurantId}/tables/{tableId} - Eliminar mesa
     */
    @DeleteMapping("/{tableId}")
    public ResponseEntity<Void> deleteTable(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId) {
        restaurantTableService.deleteTable(tableId, restaurantId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/restaurants/{restaurantId}/tables/available - Obtener mesas disponibles
     */
    @GetMapping("/available")
    public ResponseEntity<List<RestaurantTable>> getAvailableTables(@PathVariable Long restaurantId) {
        List<RestaurantTable> tables = restaurantTableService.findAvailableTablesByRestaurant(restaurantId);
        return ResponseEntity.ok(tables);
    }

    // DTOs para requests
    public static class CreateTableRequest {
        @NotNull(message = "El número de mesa es obligatorio")
        @Min(value = 1, message = "El número de mesa debe ser mayor a 0")
        private Integer tableNumber;

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser mayor a 0")
        private Integer capacity;

        private TableStatus status = TableStatus.AVAILABLE;

        // Getters y Setters
        public Integer getTableNumber() { return tableNumber; }
        public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }

        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }

        public TableStatus getStatus() { return status; }
        public void setStatus(TableStatus status) { this.status = status; }
    }

    public static class UpdateTableRequest {
        private Integer tableNumber;
        private Integer capacity;
        private TableStatus status;

        // Getters y Setters
        public Integer getTableNumber() { return tableNumber; }
        public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }

        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }

        public TableStatus getStatus() { return status; }
        public void setStatus(TableStatus status) { this.status = status; }
    }
}