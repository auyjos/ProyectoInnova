package com.innova.restaurant.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innova.restaurant.controller.ReservationController;
import com.innova.restaurant.model.entity.Reservation;
import com.innova.restaurant.model.entity.Restaurant;
import com.innova.restaurant.model.entity.RestaurantTable;
import com.innova.restaurant.model.entity.User;
import com.innova.restaurant.model.enums.ReservationStatus;
import com.innova.restaurant.repository.jpa.ReservationRepository;
import com.innova.restaurant.repository.jpa.RestaurantRepository;
import com.innova.restaurant.repository.jpa.RestaurantTableRepository;
import com.innova.restaurant.repository.jpa.UserRepository;
import com.innova.restaurant.service.ReservationService;

/**
 * Implementación del servicio de reservas
 * 
 * Gestiona toda la lógica de negocio relacionada con las reservas:
 * - Creación y validación de reservas
 * - Gestión de estados de reserva
 * - Validaciones de disponibilidad
 * - Operaciones de consulta y filtrado
 * 
 * Utiliza métodos automáticos de Spring Data JPA siguiendo el patrón establecido
 * sin usar anotaciones @Query personalizadas.
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Reservation> findAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public Reservation createReservation(ReservationController.CreateReservationRequest request) {
        logger.info("Creando nueva reserva para restaurante ID: {} en fecha: {}", 
                   request.getRestaurantId(), request.getReservationDateTime());
        
        logger.debug("Request data: userId={}, restaurantId={}, tableId={}, dateTime={}, guests={}", 
                    request.getUserId(), request.getRestaurantId(), request.getTableId(), 
                    request.getReservationDateTime(), request.getNumberOfPeople());

        // Validar que el restaurante existe y está activo
        logger.debug("Buscando restaurante con ID: {}", request.getRestaurantId());
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
            .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        logger.debug("Restaurante encontrado: {}, activo: {}", restaurant.getName(), restaurant.getIsActive());

        if (!restaurant.getIsActive()) {
            throw new RuntimeException("El restaurante no está disponible para reservas");
        }

        // Validar que el usuario existe
        logger.debug("Buscando usuario con ID: {}", request.getUserId());
        User customer = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        logger.debug("Usuario encontrado: {}", customer.getUsername());

        // Validar que la mesa existe
        logger.debug("Buscando mesa con ID: {}", request.getTableId());
        RestaurantTable table = restaurantTableRepository.findById(request.getTableId())
            .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        logger.debug("Mesa encontrada: mesa #{}, capacidad: {}", table.getTableNumber(), table.getCapacity());

        // Validar que el número de personas no exceda la capacidad de la mesa
        if (request.getNumberOfPeople() > table.getCapacity()) {
            throw new RuntimeException(
                String.format("El número de personas (%d) excede la capacidad de la mesa (%d)", 
                             request.getNumberOfPeople(), table.getCapacity())
            );
        }

        // Validar que la fecha de reserva es futura
        if (request.getReservationDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden hacer reservas en el pasado");
        }

        // Validar horarios de operación del restaurante
        LocalTime reservationTime = request.getReservationDateTime().toLocalTime();
        if (reservationTime.isBefore(restaurant.getOpeningTime()) || 
            reservationTime.isAfter(restaurant.getClosingTime())) {
            throw new RuntimeException("La hora de reserva está fuera del horario de operación");
        }

        // Validar disponibilidad de la mesa
        logger.debug("Validando disponibilidad de mesa...");
        validateTableAvailability(request.getTableId(), request.getReservationDateTime());

        // Crear la reserva
        logger.debug("Creando entidad Reservation...");
        Reservation reservation = new Reservation(
            customer, restaurant, table, 
            request.getReservationDateTime(), 
            request.getNumberOfPeople(),
            request.getSpecialRequests()
        );
        
        logger.debug("Entidad creada, ID antes de save: {}", reservation.getId());
        logger.debug("Guardando reserva en base de datos...");
        Reservation savedReservation = reservationRepository.save(reservation);
        logger.info("Reserva creada exitosamente con ID: {}", savedReservation.getId());

        return savedReservation;
    }

    @Override
    public Reservation updateReservation(Long id, ReservationController.UpdateReservationRequest request) {
        logger.info("Actualizando reserva ID: {}", id);

        Reservation existingReservation = findReservationById(id);

        // Solo se pueden modificar reservas pendientes
        if (existingReservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Solo se pueden modificar reservas pendientes");
        }

        // Validar nueva fecha si se está cambiando
        if (request.getReservationDateTime() != null) {
            if (request.getReservationDateTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("No se pueden hacer reservas en el pasado");
            }
            validateTableAvailability(existingReservation.getTable().getId(), 
                                    request.getReservationDateTime());
            existingReservation.setReservationDate(request.getReservationDateTime());
        }

        // Actualizar otros campos permitidos
        if (request.getNumberOfPeople() != null) {
            existingReservation.setNumberOfPeople(request.getNumberOfPeople());
        }

        if (request.getSpecialRequests() != null) {
            existingReservation.setSpecialRequests(request.getSpecialRequests());
        }

        Reservation savedReservation = reservationRepository.save(existingReservation);
        logger.info("Reserva actualizada exitosamente");

        return savedReservation;
    }

    @Override
    public Reservation updateStatus(Long id, String status) {
        logger.info("Actualizando estado de reserva ID: {} a: {}", id, status);

        Reservation reservation = findReservationById(id);
        ReservationStatus newStatus;

        try {
            newStatus = ReservationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de reserva inválido: " + status);
        }

        // Validar transiciones de estado válidas
        validateStatusTransition(reservation.getStatus(), newStatus);

        reservation.setStatus(newStatus);

        Reservation updatedReservation = reservationRepository.save(reservation);
        logger.info("Estado de reserva actualizado exitosamente");

        return updatedReservation;
    }

    @Override
    public void cancelReservation(Long id) {
        logger.info("Cancelando reserva ID: {}", id);

        Reservation reservation = findReservationById(id);

        // Solo se pueden cancelar reservas pendientes o confirmadas
        if (reservation.getStatus() == ReservationStatus.CANCELLED || 
            reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new RuntimeException("No se puede cancelar una reserva en estado: " + reservation.getStatus());
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        
        logger.info("Reserva cancelada exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByUser(Long userId) {
        return reservationRepository.findByCustomerIdOrderByReservationDateDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByRestaurant(Long restaurantId, String status) {
        if (status == null || status.isEmpty()) {
            return reservationRepository.findByRestaurantIdOrderByReservationDateAsc(restaurantId);
        }

        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
            return reservationRepository.findByStatusOrderByReservationDateAsc(reservationStatus)
                .stream()
                .filter(r -> r.getRestaurant().getId().equals(restaurantId))
                .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de reserva inválido: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByDateRange(Long restaurantId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByRestaurantIdAndReservationDateBetweenOrderByReservationDateAsc(
            restaurantId, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findTodayReservations(Long restaurantId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        return reservationRepository.findByRestaurantIdAndReservationDateBetweenOrderByReservationDateAsc(
            restaurantId, startOfDay, endOfDay);
    }

    @Override
    public Reservation confirmReservation(Long id) {
        return updateStatus(id, ReservationStatus.CONFIRMED.name());
    }

    @Override
    public Reservation checkInReservation(Long id) {
        logger.info("Registrando check-in para reserva ID: {}", id);

        Reservation reservation = findReservationById(id);

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new RuntimeException("Solo se puede hacer check-in de reservas confirmadas");
        }

        // Validar que la hora de check-in es apropiada (dentro de 30 minutos de la reserva)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationTime = reservation.getReservationDate();
        
        if (now.isBefore(reservationTime.minusMinutes(30)) || 
            now.isAfter(reservationTime.plusHours(2))) {
            throw new RuntimeException("Check-in fuera del tiempo permitido");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        Reservation checkedInReservation = reservationRepository.save(reservation);
        
        logger.info("Check-in registrado exitosamente");
        return checkedInReservation;
    }

    /**
     * Valida que una mesa esté disponible para una fecha específica
     */
    private void validateTableAvailability(Long tableId, LocalDateTime dateTime) {
        // Obtener reservas existentes en una ventana de 2 horas alrededor de la hora solicitada
        LocalDateTime start = dateTime.minusHours(1);
        LocalDateTime end = dateTime.plusHours(1);
        
        List<Reservation> existingReservations = reservationRepository
            .findByTableIdAndReservationDateBetweenOrderByReservationDateAsc(tableId, start, end);
        
        // Filtrar solo reservas activas (no canceladas)
        List<Reservation> activeReservations = existingReservations.stream()
            .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
            .collect(Collectors.toList());
        
        if (!activeReservations.isEmpty()) {
            throw new RuntimeException("La mesa no está disponible para la fecha y hora solicitada");
        }
    }

    /**
     * Valida las transiciones de estado permitidas
     */
    private void validateStatusTransition(ReservationStatus currentStatus, ReservationStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                if (newStatus != ReservationStatus.CONFIRMED && newStatus != ReservationStatus.CANCELLED) {
                    throw new RuntimeException("Transición de estado no válida desde PENDING a " + newStatus);
                }
                break;
            case CONFIRMED:
                if (newStatus != ReservationStatus.COMPLETED && 
                    newStatus != ReservationStatus.CANCELLED && 
                    newStatus != ReservationStatus.NO_SHOW) {
                    throw new RuntimeException("Transición de estado no válida desde CONFIRMED a " + newStatus);
                }
                break;
            case COMPLETED:
            case CANCELLED:
            case NO_SHOW:
                throw new RuntimeException("No se puede cambiar el estado de una reserva " + currentStatus);
            default:
                throw new RuntimeException("Estado actual desconocido: " + currentStatus);
        }
    }
}