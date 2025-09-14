package com.innova.restaurant.model.enums;

/**
 * Estado de disponibilidad de una mesa
 */
public enum TableStatus {
    AVAILABLE("Disponible"),
    OCCUPIED("Ocupada"),
    RESERVED("Reservada"),
    OUT_OF_SERVICE("Fuera de servicio");

    private final String displayName;

    TableStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}