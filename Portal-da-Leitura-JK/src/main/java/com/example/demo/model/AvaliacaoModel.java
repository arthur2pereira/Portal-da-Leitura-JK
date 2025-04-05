package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "avaliacoes")
public class AvaliacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relação obrigatória com o aluno (avaliador)
    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id", referencedColumnName = "matricula")
    private AlunoModel aluno;

    // Relação obrigatória com o livro avaliado
    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id")
    private LivroModel livro;

    // Nota obrigatória entre 0 e 5
    @NotNull
    @Min(0)
    @Max(5)
    private Integer nota;

    // Comentário opcional, com no máximo 500 caracteres
    @Size(max = 500)
    private String comentario;
}
