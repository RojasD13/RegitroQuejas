package com.uptc.edu.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quejas")
@Getter
@Setter
public class Queja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_queja")
    private Long id;

    @Column(name = "descripcion", nullable = false, length = 1000)
    @NotNull
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    @JsonIgnore
    private Empresa empresa;

    @Column(name = "visible", nullable = false)
    private boolean isVisible = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private Estado estado = Estado.PROCESO;

    @OneToMany(mappedBy = "queja", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comentarios = new ArrayList<>();

    public void addComentario(Comment comentario) {
        comentarios.add(comentario);
        comentario.setComplaint(this);
    }

    public void removeComentario(Comment comentario) {
        comentarios.remove(comentario);
        comentario.setComplaint(null);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
}
