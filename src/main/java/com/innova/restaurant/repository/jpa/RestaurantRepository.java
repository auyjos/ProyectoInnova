package com.innova.restaurant.repository.jpa;

import com.innova.restaurant.model.entity.Restaurant;
import com.innova.restaurant.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
     * Busca restaurantes por nombre usando LIKE
     *
     * @param name nombre del restaurante
     * @return lista de restaurantes que coinciden con el nombre
     */
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Restaurant> findByNameContaining(@Param("name") String name);

    /**
     * Busca restaurantes por dirección usando LIKE
     *
     * @param address dirección del restaurante
     * @return lista de restaurantes que coinciden con la dirección
     */
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Restaurant> findByAddressContaining(@Param("address") String address);

    /**
     * Busca restaurantes por capacidad mínima
     *
     * @param minCapacity capacidad mínima
     * @return lista de restaurantes con capacidad mayor o igual a la especificada
     */
    List<Restaurant> findByMaxCapacityGreaterThanEqual(Integer minCapacity);

    /**
     * Busca restaurantes abiertos en un horario específico
     *
     * @param time horario a verificar
     * @return lista de restaurantes abiertos en el horario especificado
     */
    @Query("SELECT r FROM Restaurant r WHERE r.openingTime <= :time AND r.closingTime >= :time AND r.isActive = true")
    List<Restaurant> findOpenAtTime(@Param("time") LocalTime time);

    /**
     * Busca restaurantes por criterios múltiples
     *
     * @param searchTerm término de búsqueda para nombre o dirección
     * @param minCapacity capacidad mínima
     * @param isActive estado activo
     * @return lista de restaurantes que cumplen los criterios
     */
    @Query("SELECT r FROM Restaurant r WHERE " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.address) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "r.maxCapacity >= :minCapacity AND " +
           "r.isActive = :isActive")
    List<Restaurant> findByCriteria(@Param("searchTerm") String searchTerm, 
                                   @Param("minCapacity") Integer minCapacity, 
                                   @Param("isActive") Boolean isActive);

    /**
     * Cuenta restaurantes por propietario
     *
     * @param owner el propietario del restaurante
     * @return cantidad de restaurantes del propietario
     */
    long countByOwner(User owner);

    /**
     * Verifica si existe un restaurante con el nombre especificado para un propietario
     *
     * @param name nombre del restaurante
     * @param owner propietario del restaurante
     * @return true si existe un restaurante con ese nombre para el propietario
     */
    boolean existsByNameAndOwner(String name, User owner);
}