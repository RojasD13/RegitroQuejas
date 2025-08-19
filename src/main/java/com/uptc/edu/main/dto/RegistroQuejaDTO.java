package com.uptc.edu.main.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
    public class RegistroQuejaDTO {

    @NotNull
    private Long empresaId;

    @NotNull
    @Size(min = 1, max = 1000, message = "La descripción debe tener entre 1 y 1000 caracteres")
    private String descripcion;

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
