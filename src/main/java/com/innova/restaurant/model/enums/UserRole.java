package com.innova.restaurant.model.enums;

/**
 * Enumeración para los tipos de usuario del sistema
 */
public enum UserRole {
    CUSTOMER("Cliente"),
    RESTAURANT_OWNER("Propietario de Restaurante"),
    ADMIN("Administrador");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}