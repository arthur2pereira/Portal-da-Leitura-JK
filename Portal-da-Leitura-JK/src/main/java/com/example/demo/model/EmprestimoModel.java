package com.example.demo.model;

import jakarta.persistence.*;
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
    private Long id;

    @ManyToOne
    private AlunoModel aluno;

    @ManyToOne
    private LivroModel livro;

    @ManyToOne
    @JoinColumn(name = "id_bibliotecario", nullable = false)
    private BibliotecarioModel bibliotecario;

    private LocalDate dataEmprestimo;

    private LocalDate dataVencimento;

    private LocalDate dataDevolucao;
}

