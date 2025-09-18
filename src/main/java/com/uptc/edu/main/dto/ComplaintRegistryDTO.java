package com.uptc.edu.main.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComplaintRegistryDTO {

    @NotNull
    private Long companyId;

    @NotNull
    @Size(min = 1, max = 1000, message = "La descripción debe tener entre 1 y 1000 caracteres")
    private String descripcion;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
