package com.innova.restaurant.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.innova.restaurant.model.entity.Reservation;
import com.innova.restaurant.service.ReservationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Controlador REST para gestión de reservaciones
 * Cumple con Richardson Nivel 2 - Verbos HTTP y códigos de estado correctos
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * GET /api/reservations - Obtener todas las reservaciones con paginación
     */
    @GetMapping
    public ResponseEntity<Page<Reservation>> getAllReservations(Pageable pageable) {
        Page<Reservation> reservations = reservationService.findAllReservations(pageable);
        return ResponseEntity.ok(reservations);
    }

    /**
     * GET /api/v1/reservations/{id} - Obtener reservación por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.findReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    /**
     * POST /api/v1/reservations - Crear nueva reservación
     */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request);
        URI location = URI.create("/api/v1/reservations/" + reservation.getId());
        return ResponseEntity.created(location).body(reservation);
    }

    /**
     * PUT /api/v1/reservations/{id} - Actualizar reservación completa
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateReservationRequest request) {
        Reservation reservation = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(reservation);
    }

    /**
     * PATCH /api/v1/reservations/{id}/status - Cambiar estado de reservación
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        Reservation reservation = reservationService.updateStatus(id, status);
        return ResponseEntity.ok(reservation);
    }

    /**
     * DELETE /api/v1/reservations/{id} - Cancelar reservación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/reservations/user/{userId} - Reservaciones por usuario
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.findByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * GET /api/v1/reservations/restaurant/{restaurantId} - Reservaciones por restaurante
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Reservation>> getReservationsByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String status) {
        List<Reservation> reservations = reservationService.findByRestaurant(restaurantId, status);
        return ResponseEntity.ok(reservations);
    }

    /**
     * GET /api/v1/reservations/date-range - Reservaciones por rango de fechas
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Reservation>> getReservationsByDateRange(
            @RequestParam Long restaurantId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<Reservation> reservations = reservationService.findByDateRange(restaurantId, start, end);
        return ResponseEntity.ok(reservations);
    }

    /**
     * GET /api/v1/reservations/today/{restaurantId} - Reservaciones de hoy
     */
    @GetMapping("/today/{restaurantId}")
    public ResponseEntity<List<Reservation>> getTodayReservations(@PathVariable Long restaurantId) {
        List<Reservation> reservations = reservationService.findTodayReservations(restaurantId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * POST /api/v1/reservations/{id}/confirm - Confirmar reservación
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Reservation> confirmReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.confirmReservation(id);
        return ResponseEntity.ok(reservation);
    }

    /**
     * POST /api/v1/reservations/{id}/checkin - Check-in de reservación
     */
    @PostMapping("/{id}/checkin")
    public ResponseEntity<Reservation> checkInReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.checkInReservation(id);
        return ResponseEntity.ok(reservation);
    }

    // DTOs para requests
    public static class CreateReservationRequest {
        @NotNull(message = "El ID del usuario es obligatorio")
        private Long userId;
        
        @NotNull(message = "El ID del restaurante es obligatorio")
        private Long restaurantId;
        
        @NotNull(message = "El ID de la mesa es obligatorio")
        private Long tableId;
        
        @NotNull(message = "La fecha de reserva es obligatoria")
        @Future(message = "La fecha de reserva debe ser futura")
        private LocalDateTime reservationDateTime;
        
        @NotNull(message = "El número de personas es obligatorio")
        @Min(value = 1, message = "El número de personas debe ser mayor a 0")
        private Integer numberOfPeople;
        
        private String specialRequests;

        // Getters y Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getRestaurantId() { return restaurantId; }
        public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

        public Long getTableId() { return tableId; }
        public void setTableId(Long tableId) { this.tableId = tableId; }

        public LocalDateTime getReservationDateTime() { return reservationDateTime; }
        public void setReservationDateTime(LocalDateTime reservationDateTime) { this.reservationDateTime = reservationDateTime; }

        public Integer getNumberOfPeople() { return numberOfPeople; }
        public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }

        public String getSpecialRequests() { return specialRequests; }
        public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    }

    public static class UpdateReservationRequest {
        private LocalDateTime reservationDateTime;
        private Integer numberOfPeople;
        private String specialRequests;

        // Getters y Setters
        public LocalDateTime getReservationDateTime() { return reservationDateTime; }
        public void setReservationDateTime(LocalDateTime reservationDateTime) { this.reservationDateTime = reservationDateTime; }

        public Integer getNumberOfPeople() { return numberOfPeople; }
        public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }

        public String getSpecialRequests() { return specialRequests; }
        public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    }
}