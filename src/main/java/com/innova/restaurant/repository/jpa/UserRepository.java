package com.innova.restaurant.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.innova.restaurant.model.entity.User;
import com.innova.restaurant.model.enums.UserRole;

/**
 * Repositorio JPA para la entidad User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Busca un usuario por su email
     *
     * @param email el email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario
     *
     * @param username el nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por email o nombre de usuario
     *
     * @param email el email del usuario
     * @param username el nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    /**
     * Busca usuarios por rol
     *
     * @param role el rol del usuario
     * @return lista de usuarios con el rol especificado
     */
    List<User> findByRole(UserRole role);

    /**
     * Busca usuarios activos por rol
     *
     * @param role el rol del usuario
     * @param isActive estado activo del usuario
     * @return lista de usuarios activos con el rol especificado
     */
    List<User> findByRoleAndIsActive(UserRole role, Boolean isActive);

    /**
     * Busca usuarios por estado activo
     *
     * @param isActive estado activo del usuario
     * @return lista de usuarios con el estado especificado
     */
    List<User> findByIsActive(Boolean isActive);

    /**
     * Verifica si existe un usuario con el email especificado
     *
     * @param email el email a verificar
     * @return true si existe un usuario con ese email
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado
     *
     * @param username el nombre de usuario a verificar
     * @return true si existe un usuario con ese nombre de usuario
     */
    boolean existsByUsername(String username);

    /**
     * Cuenta usuarios por rol
     *
     * @param role el rol del usuario
     * @return cantidad de usuarios con el rol especificado
     */
    long countByRole(UserRole role);

    /**
     * Busca usuarios por nombre que contiene el término (ignora mayúsculas/minúsculas)
     *
     * @param firstName término de búsqueda para el nombre
     * @return lista de usuarios que coinciden con la búsqueda
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Busca usuarios por apellido que contiene el término (ignora mayúsculas/minúsculas)
     *
     * @param lastName término de búsqueda para el apellido
     * @return lista de usuarios que coinciden con la búsqueda
     */
    List<User> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Busca usuarios por nombre O apellido que contiene el término
     *
     * @param firstName término de búsqueda para el nombre
     * @param lastName término de búsqueda para el apellido (mismo valor que firstName)
     * @return lista de usuarios que coinciden con la búsqueda en nombre o apellido
     */
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName);
}