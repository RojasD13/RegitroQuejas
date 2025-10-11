package com.uptc.edu.main.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;   
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    
    @NotBlank(message = "El contenido del comentario no puede estar vacío")
    @Size(max = 2000, message = "El contenido del comentario no puede exceder los 2000 caracteres") 
    private String text;
    
    private LocalDateTime timestamp;
}