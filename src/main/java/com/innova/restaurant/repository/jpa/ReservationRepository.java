package com.innova.restaurant.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.innova.restaurant.model.entity.Reservation;
import com.innova.restaurant.model.enums.ReservationStatus;

/**
 * Repositorio JPA para la entidad Reservation
 * Utiliza métodos automáticos de Spring Data JPA siguiendo el patrón establecido
 * sin usar anotaciones @Query personalizadas.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    /**
     * Busca reservas por ID de cliente ordenadas por fecha descendente
     *
     * @param customerId ID del cliente
     * @return lista de reservas del cliente
     */
    List<Reservation> findByCustomerIdOrderByReservationDateDesc(Long customerId);

    /**
     * Busca reservas por ID de restaurante ordenadas por fecha ascendente
     *
     * @param restaurantId ID del restaurante
     * @return lista de reservas del restaurante
     */
    List<Reservation> findByRestaurantIdOrderByReservationDateAsc(Long restaurantId);

    /**
     * Busca reservas de un restaurante en un rango de fechas ordenadas por fecha
     *
     * @param restaurantId ID del restaurante
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de reservas en el rango
     */
    List<Reservation> findByRestaurantIdAndReservationDateBetweenOrderByReservationDateAsc(
        Long restaurantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca reservas en un rango de fechas ordenadas por fecha
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de reservas en el rango
     */
    List<Reservation> findByReservationDateBetweenOrderByReservationDateAsc(
        LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca reservas por estado ordenadas por fecha
     *
     * @param status estado de la reserva
     * @return lista de reservas con el estado especificado
     */
    List<Reservation> findByStatusOrderByReservationDateAsc(ReservationStatus status);

    /**
     * Cuenta reservas de un restaurante en un rango de fechas
     *
     * @param restaurantId ID del restaurante
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return número de reservas
     */
    long countByRestaurantIdAndReservationDateBetween(
        Long restaurantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca reservas de un restaurante en un rango de fechas excluyendo un estado específico
     *
     * @param restaurantId ID del restaurante
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @param status estado a excluir
     * @return lista de reservas
     */
    List<Reservation> findByRestaurantIdAndReservationDateBetweenAndStatusNotOrderByReservationDateAsc(
        Long restaurantId, LocalDateTime startDate, LocalDateTime endDate, ReservationStatus status);

    /**
     * Busca reservas de hoy para un restaurante
     *
     * @param restaurantId ID del restaurante
     * @param startOfDay inicio del día
     * @param endOfDay fin del día
     * @return lista de reservas de hoy
     */
    List<Reservation> findByRestaurantIdAndReservationDateBetweenAndStatusInOrderByReservationDateAsc(
        Long restaurantId, LocalDateTime startOfDay, LocalDateTime endOfDay, List<ReservationStatus> statuses);

    /**
     * Busca reservas por mesa en un rango de fechas
     *
     * @param tableId ID de la mesa
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de reservas de la mesa
     */
    List<Reservation> findByTableIdAndReservationDateBetweenOrderByReservationDateAsc(
        Long tableId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca reservas activas (no canceladas) de un cliente
     *
     * @param customerId ID del cliente
     * @param status estado a excluir (generalmente CANCELLED)
     * @return lista de reservas activas del cliente
     */
    List<Reservation> findByCustomerIdAndStatusNotOrderByReservationDateDesc(
        Long customerId, ReservationStatus status);

    /**
     * Busca reservas futuras de un cliente
     *
     * @param customerId ID del cliente
     * @param currentDateTime fecha y hora actual
     * @return lista de reservas futuras
     */
    List<Reservation> findByCustomerIdAndReservationDateAfterOrderByReservationDateAsc(
        Long customerId, LocalDateTime currentDateTime);

    /**
     * Busca reservas que necesitan confirmación (creadas hace más de X tiempo)
     *
     * @param cutoffDateTime tiempo límite para confirmación
     * @param status estado pendiente
     * @return lista de reservas pendientes de confirmación
     */
    List<Reservation> findByCreatedAtBeforeAndStatusOrderByCreatedAtAsc(
        LocalDateTime cutoffDateTime, ReservationStatus status);
}