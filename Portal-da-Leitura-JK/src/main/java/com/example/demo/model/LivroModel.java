package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livros")
public class LivroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "livro_id")
    private Long livroId;

    @NotBlank
    @Size(max = 150)
    private String titulo;

    @NotBlank
    @Size(max = 100)
    private String autor;

    @NotBlank
    @Size(max = 50)
    private String genero;

    @Size(max = 100)
    private String curso;

    @Min(1500)
    @Max(2100)
    private Integer anoPublicacao;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Min(0)
    private Integer quantidade = 1;
}
