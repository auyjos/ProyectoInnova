package com.innova.restaurant.repository.jpa;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innova.restaurant.model.entity.Restaurant;
import com.innova.restaurant.model.entity.User;

/**
 * Repositorio JPA para la entidad Restaurant
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * Busca restaurantes por propietario
     *
     * @param owner el propietario del restaurante
     * @return lista de restaurantes del propietario
     */
    List<Restaurant> findByOwner(User owner);

    /**
     * Busca restaurantes por estado activo
     *
     * @param isActive estado activo del restaurante
     * @return lista de restaurantes con el estado especificado
     */
    List<Restaurant> findByIsActive(Boolean isActive);

    /**
     * Busca restaurantes activos por propietario
     *
     * @param owner el propietario del restaurante
     * @param isActive estado activo del restaurante
     * @return lista de restaurantes activos del propietario
     */
    List<Restaurant> findByOwnerAndIsActive(User owner, Boolean isActive);

    /**
     * Busca restaurantes por nombre usando LIKE - Query method automático
     */
    List<Restaurant> findByNameContainingIgnoreCase(String name);

    /**
     * Busca restaurantes por nombre y estado activo - Query method automático
     */
    List<Restaurant> findByNameContainingIgnoreCaseAndIsActive(String name, Boolean isActive);

    /**
     * Busca restaurantes por propietario ID y estado activo - Query method automático
     */
    List<Restaurant> findByOwnerIdAndIsActive(Long ownerId, Boolean isActive);

    /**
     * Busca restaurantes por dirección usando LIKE - Query method automático
     */
    List<Restaurant> findByAddressContainingIgnoreCase(String address);

    /**
     * Busca restaurantes abiertos en un horario específico - Query method automático
     */
    List<Restaurant> findByOpeningTimeLessThanEqualAndClosingTimeGreaterThanEqualAndIsActive(
        LocalTime openingTime, LocalTime closingTime, Boolean isActive);

    /**
     * Busca restaurantes por capacidad mínima - Query method automático
     */
    List<Restaurant> findByMaxCapacityGreaterThanEqual(Integer minCapacity);

    /**
     * Busca restaurantes activos por capacidad mínima - Query method automático
     */
    List<Restaurant> findByMaxCapacityGreaterThanEqualAndIsActive(Integer minCapacity, Boolean isActive);

    /**
     * Cuenta restaurantes por propietario - Query method automático
     */
    long countByOwner(User owner);

    /**
     * Cuenta restaurantes activos por propietario - Query method automático
     */
    long countByOwnerAndIsActive(User owner, Boolean isActive);

    /**
     * Verifica si existe un restaurante con el nombre especificado para un propietario - Query method automático
     */
    boolean existsByNameAndOwner(String name, User owner);

    /**
     * Verifica si existe un restaurante activo con el nombre especificado para un propietario - Query method automático
     */
    boolean existsByNameAndOwnerAndIsActive(String name, User owner, Boolean isActive);
}