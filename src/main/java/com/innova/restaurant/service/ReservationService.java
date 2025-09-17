package com.innova.restaurant.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.innova.restaurant.controller.ReservationController;
import com.innova.restaurant.model.entity.Reservation;

/**
 * Interfaz del servicio de reservaciones
 */
public interface ReservationService {

    /**
     * Encuentra todas las reservaciones con paginación
     */
    Page<Reservation> findAllReservations(Pageable pageable);

    /**
     * Encuentra una reservación por ID
     */
    Reservation findReservationById(Long id);

    /**
     * Crea una nueva reservación
     */
    Reservation createReservation(ReservationController.CreateReservationRequest request);

    /**
     * Actualiza una reservación completa
     */
    Reservation updateReservation(Long id, ReservationController.UpdateReservationRequest request);

    /**
     * Actualiza el estado de una reservación
     */
    Reservation updateStatus(Long id, String status);

    /**
     * Cancela una reservación
     */
    void cancelReservation(Long id);

    /**
     * Encuentra reservaciones por usuario
     */
    List<Reservation> findByUser(Long userId);

    /**
     * Encuentra reservaciones por restaurante
     */
    List<Reservation> findByRestaurant(Long restaurantId, String status);

    /**
     * Encuentra reservaciones por rango de fechas
     */
    List<Reservation> findByDateRange(Long restaurantId, LocalDateTime start, LocalDateTime end);

    /**
     * Encuentra reservaciones de hoy
     */
    List<Reservation> findTodayReservations(Long restaurantId);

    /**
     * Confirma una reservación
     */
    Reservation confirmReservation(Long id);

    /**
     * Check-in de una reservación
     */
    Reservation checkInReservation(Long id);
}