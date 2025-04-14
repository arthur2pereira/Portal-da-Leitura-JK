package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emprestimos")
public class EmprestimoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emprestimoId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula_aluno", referencedColumnName = "matricula")
    private AlunoModel aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id")
    private LivroModel livro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bibliotecario_id", nullable = false)
    private BibliotecarioModel bibliotecario;

    @NotNull
    private LocalDate dataEmprestimo;

    @NotNull
    private LocalDate dataVencimento;

    private LocalDate dataDevolucao;

    private int renovacoes = 0;

    private String status;
}

