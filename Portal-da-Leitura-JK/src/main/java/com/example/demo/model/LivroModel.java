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
    private Long id;

    @NotBlank
    @Size(max = 150)
    private String titulo; // nome do livro não pode vir vazio

    @NotBlank
    @Size(max = 100)
    private String autor; // quem escreveu a obra, também obrigatório

    @NotBlank
    @Size(max = 50)
    private String genero; // tipo "ficção", "romance", etc.

    // Curso da escola relacionado ao livro, tipo "Informática", mas pode ser nulo
    @Size(max = 100)
    private String curso;

    @Min(1500) // só pra garantir que ninguém coloque o ano 1300
    @Max(2100) // não vai dar pra publicar livro de 3000
    private Integer anoPublicacao;

    // Descrição grandona do livro, tipo sinopse, por isso é TEXT
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Min(0) // quantidade não pode ser negativa
    private Integer quantidade = 1; // por padrão tem 1 exemplar
}
