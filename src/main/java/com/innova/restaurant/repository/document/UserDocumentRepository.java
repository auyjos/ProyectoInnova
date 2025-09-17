package com.innova.restaurant.repository.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.innova.restaurant.model.document.UserDocument;
import com.innova.restaurant.model.enums.UserRole;

/**
 * Repositorio MongoDB para UserDocument utilizando query methods sin @Query
 */
@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, String> {

    /**
     * Busca un usuario por su email
     *
     * @param email el email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserDocument> findByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario
     *
     * @param username el nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserDocument> findByUsername(String username);

    /**
     * Busca un usuario por email o nombre de usuario
     * Método derivado automático - Spring Data MongoDB genera la query
     *
     * @param email el email del usuario
     * @param username el nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserDocument> findByEmailOrUsername(String email, String username);

    /**
     * Busca usuarios por rol
     *
     * @param role el rol del usuario
     * @return lista de usuarios con el rol especificado
     */
    List<UserDocument> findByRole(UserRole role);

    /**
     * Busca usuarios activos por rol
     *
     * @param role el rol del usuario
     * @param isActive estado activo del usuario
     * @return lista de usuarios activos con el rol especificado
     */
    List<UserDocument> findByRoleAndIsActive(UserRole role, Boolean isActive);

    /**
     * Busca usuarios por estado activo
     *
     * @param isActive estado activo del usuario
     * @return lista de usuarios con el estado especificado
     */
    List<UserDocument> findByIsActive(Boolean isActive);

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
     * Método derivado automático - Spring Data MongoDB genera la query
     */
    List<UserDocument> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Busca usuarios por apellido que contiene el término (ignora mayúsculas/minúsculas)
     * Método derivado automático - Spring Data MongoDB genera la query
     */
    List<UserDocument> findByLastNameContainingIgnoreCase(String lastName);
    
    /**
     * Busca usuarios por nombre O apellido que contiene el término
     * Método derivado automático - Spring Data MongoDB genera la query
     */
    List<UserDocument> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName);
}