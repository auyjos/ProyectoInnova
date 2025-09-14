package com.innova.restaurant.model.enums;

/**
 * Estado de una reserva en el sistema
 */
public enum ReservationStatus {
    PENDING("Pendiente"),
    CONFIRMED("Confirmada"),
    CANCELLED("Cancelada"),
    COMPLETED("Completada"),
    NO_SHOW("No se presentó");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}