package com.innova.restaurant.repository.jpa;

import org.springframework.data.jpa.domain.Specification;

import com.innova.restaurant.model.entity.User;
import com.innova.restaurant.model.enums.UserRole;

/**
 * Especificaciones JPA para consultas dinámicas de Usuario
 * 
 * Esta clase proporciona métodos estáticos que devuelven Specification<User>
 * para crear consultas dinámicas complejas sin escribir SQL/JPQL manual.
 */
public class UserSpecifications {

    /**
     * Especificación para buscar usuarios por nombre que contiene el término
     * 
     * @param firstName término de búsqueda para el nombre (puede ser null)
     * @return especificación para filtrar por nombre
     */
    public static Specification<User> hasFirstNameContaining(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.trim().isEmpty()) {
                return null; // No aplicar filtro si el término está vacío
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("firstName")),
                "%" + firstName.toLowerCase() + "%"
            );
        };
    }

    /**
     * Especificación para buscar usuarios por apellido que contiene el término
     * 
     * @param lastName término de búsqueda para el apellido (puede ser null)
     * @return especificación para filtrar por apellido
     */
    public static Specification<User> hasLastNameContaining(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("lastName")),
                "%" + lastName.toLowerCase() + "%"
            );
        };
    }

    /**
     * Especificación para buscar usuarios por rol específico
     * 
     * @param role rol del usuario (puede ser null)
     * @return especificación para filtrar por rol
     */
    public static Specification<User> hasRole(UserRole role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    /**
     * Especificación para buscar usuarios por estado activo
     * 
     * @param isActive estado activo del usuario (puede ser null)
     * @return especificación para filtrar por estado activo
     */
    public static Specification<User> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    /**
     * Especificación para buscar usuarios por email que contiene el término
     * 
     * @param email término de búsqueda para el email (puede ser null)
     * @return especificación para filtrar por email
     */
    public static Specification<User> hasEmailContaining(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("email")),
                "%" + email.toLowerCase() + "%"
            );
        };
    }

    /**
     * Especificación para buscar usuarios por username que contiene el término
     * 
     * @param username término de búsqueda para el username (puede ser null)
     * @return especificación para filtrar por username
     */
    public static Specification<User> hasUsernameContaining(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("username")),
                "%" + username.toLowerCase() + "%"
            );
        };
    }

    /**
     * Especificación combinada para buscar en nombre OR apellido
     * 
     * @param searchTerm término de búsqueda para nombre o apellido
     * @return especificación para filtrar por nombre o apellido
     */
    public static Specification<User> hasNameContaining(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return null;
            }
            String likePattern = "%" + searchTerm.toLowerCase() + "%";
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern)
            );
        };
    }
}