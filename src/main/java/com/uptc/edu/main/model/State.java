package com.uptc.edu.main.model;

public enum State {
    PROCESO("En Proceso"),
    REVISION("En Revisión"),
    CERRADO("Cerrado");

    private final String displayName;
    State(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}