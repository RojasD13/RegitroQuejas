package com.uptc.edu.main.model;

public enum State {
    PROCESO("Abierta"),
    REVISION("En Revisión"),
    CERRADO("Cerrada");

    private final String displayName;
    State(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}