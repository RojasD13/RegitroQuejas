package com.uptc.edu.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime date;

    @Column(name = "contenido", nullable = false, length = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_queja", nullable = false)
    private Queja complaint;

    public Comment() { }

    public Comment(String contenido, Queja queja) {
        this.date = LocalDateTime.now();
        this.content = contenido;
        this.complaint = queja;
    }

    public Queja getComplaint() {
        return complaint;
    }

    public void setComplaint(Queja queja) {
        this.complaint = queja;
    }
}
