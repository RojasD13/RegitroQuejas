package com.uptc.edu.main.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "complaints")
@Getter @Setter @NoArgsConstructor
public class Complaint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Long id;

    @Column(name = "description", nullable = false, length = 1000)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore 
    private Company company;

    @Column(name = "visible", nullable = false)
    private boolean isVisible = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private State state = State.PROCESO;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Complaint(String description, Company company) {
        this.description = description;
        this.company = company;
    }
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setComplaint(this);
    }
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setComplaint(null);
    }
}