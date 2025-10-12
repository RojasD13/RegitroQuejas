package com.uptc.edu.main.dto;  
public class ComplaintCompanyDTO {
    private Long id;
    private String descripcion;
    private String companyName;
    private Boolean isVisible;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public Boolean getIsVisible() {
        return isVisible;
    }
    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
}
