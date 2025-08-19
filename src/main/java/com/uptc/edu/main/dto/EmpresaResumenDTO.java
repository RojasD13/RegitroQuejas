package com.uptc.edu.main.dto;



public class EmpresaResumenDTO {
    private Long id;
    private String nombreEmpresa;
    private long totalQuejas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public long getTotalQuejas() {
        return totalQuejas;
    }

    public void setTotalQuejas(long totalQuejas) {
        this.totalQuejas = totalQuejas;
    }

    
}

    
