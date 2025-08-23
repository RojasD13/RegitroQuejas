package com.uptc.edu.main.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "empresas")

public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long id;

    @Column(name = "nombre_empresa", nullable = false, length = 100)
    @NotNull
    private String nombreEmpresa;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Queja> quejas;

    
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
    public List<Queja> getQuejas() {
        return quejas;
    }

}
