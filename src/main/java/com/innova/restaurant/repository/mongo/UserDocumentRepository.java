package com.innova.restaurant.repository.mongo;

import com.innova.restaurant.model.document.UserDocument;
import com.innova.restaurant.model.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio MongoDB para UserDocument
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
     *
     * @param email el email del usuario
     * @param username el nombre de usuario
     * @return Optional con el usuario encontrado
     */
    @Query("{ $or: [ { 'email': ?0 }, { 'username': ?1 } ] }")
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
     * Busca usuarios por nombre o apellido usando regex
     *
     * @param searchTerm término de búsqueda
     * @return lista de usuarios que coinciden con la búsqueda
     */
    @Query("{ $or: [ " +
           "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
           "{ 'lastName': { $regex: ?0, $options: 'i' } } " +
           "] }")
    List<UserDocument> findByNameContaining(String searchTerm);
}