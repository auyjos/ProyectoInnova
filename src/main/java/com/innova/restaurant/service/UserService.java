package com.innova.restaurant.service;

import com.innova.restaurant.dto.UserDto;
import com.innova.restaurant.dto.CreateUserDto;
import com.innova.restaurant.dto.UpdateUserDto;
import com.innova.restaurant.model.enums.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para operaciones de usuario
 * 
 * Define el contrato para la gestión de usuarios, permitiendo
 * implementaciones diferentes para JPA y MongoDB
 */
public interface UserService {

    /**
     * Crea un nuevo usuario
     *
     * @param createUserDto datos del usuario a crear
     * @return el usuario creado
     */
    UserDto createUser(CreateUserDto createUserDto);

    /**
     * Actualiza un usuario existente
     *
     * @param id id del usuario a actualizar
     * @param updateUserDto datos del usuario a actualizar
     * @return el usuario actualizado
     */
    UserDto updateUser(String id, UpdateUserDto updateUserDto);

    /**
     * Obtiene un usuario por su ID
     *
     * @param id id del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserDto> getUserById(String id);

    /**
     * Obtiene un usuario por su email
     *
     * @param email email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserDto> getUserByEmail(String email);

    /**
     * Obtiene un usuario por su nombre de usuario
     *
     * @param username nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserDto> getUserByUsername(String username);

    /**
     * Obtiene todos los usuarios
     *
     * @return lista de todos los usuarios
     */
    List<UserDto> getAllUsers();

    /**
     * Obtiene usuarios por rol
     *
     * @param role rol del usuario
     * @return lista de usuarios con el rol especificado
     */
    List<UserDto> getUsersByRole(UserRole role);

    /**
     * Obtiene usuarios activos
     *
     * @param isActive estado activo del usuario
     * @return lista de usuarios con el estado especificado
     */
    List<UserDto> getUsersByActiveStatus(Boolean isActive);

    /**
     * Busca usuarios por nombre o apellido
     *
     * @param searchTerm término de búsqueda
     * @return lista de usuarios que coinciden con la búsqueda
     */
    List<UserDto> searchUsersByName(String searchTerm);

    /**
     * Elimina un usuario por su ID
     *
     * @param id id del usuario a eliminar
     * @return true si el usuario fue eliminado exitosamente
     */
    boolean deleteUser(String id);

    /**
     * Activa o desactiva un usuario
     *
     * @param id id del usuario
     * @param isActive nuevo estado activo
     * @return el usuario actualizado
     */
    UserDto toggleUserActiveStatus(String id, Boolean isActive);

    /**
     * Verifica si existe un usuario con el email especificado
     *
     * @param email email a verificar
     * @return true si existe un usuario con ese email
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado
     *
     * @param username nombre de usuario a verificar
     * @return true si existe un usuario con ese nombre de usuario
     */
    boolean existsByUsername(String username);

    /**
     * Cuenta usuarios por rol
     *
     * @param role rol del usuario
     * @return cantidad de usuarios con el rol especificado
     */
    long countUsersByRole(UserRole role);
}