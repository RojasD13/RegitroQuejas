package com.uptc.edu.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Getter
@Setter
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "contenido", nullable = false, length = 2000)
    private String contenido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_queja", nullable = false)
    private Queja queja;

    public Comentario() { }

    public Comentario(String contenido, Queja queja) {
        this.fecha = LocalDateTime.now();
        this.contenido = contenido;
        this.queja = queja;
    }

    public Queja getQueja() {
        return queja;
    }

    public void setQueja(Queja queja) {
        this.queja = queja;
    }
}
