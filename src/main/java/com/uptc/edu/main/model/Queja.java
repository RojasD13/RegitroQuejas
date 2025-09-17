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
    private List<Comentario> comentarios = new ArrayList<>();

    public void addComentario(Comentario comentario) {
        comentarios.add(comentario);
        comentario.setQueja(this);
    }

    public void removeComentario(Comentario comentario) {
        comentarios.remove(comentario);
        comentario.setQueja(null);
    }

}
