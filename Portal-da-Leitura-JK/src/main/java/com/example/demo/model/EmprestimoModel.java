package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity // Tabela de empréstimos
@Data // Lombok cuida dos getters, setters e tal
@NoArgsConstructor // Construtor vazio
@AllArgsConstructor // Construtor com tudo
@Table(name = "emprestimos") // Nome da tabela no banco
public class EmprestimoModel {

    @Id // id do empréstimo
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto incrementa
    private Long id;

    @ManyToOne(optional = false) // aluno que fez o empréstimo
    @JoinColumn(name = "aluno_id", referencedColumnName = "matricula") // usa a matrícula como referência
    private AlunoModel aluno;

    @ManyToOne(optional = false) // livro que foi emprestado
    @JoinColumn(name = "livro_id") // coluna de referência no banco
    private LivroModel livro;

    @ManyToOne(optional = false) // bibliotecário que fez o processo
    @JoinColumn(name = "id_bibliotecario", nullable = false) // FK do bibliotecário
    private BibliotecarioModel bibliotecario;

    @NotNull // data em que pegou o livro
    private LocalDate dataEmprestimo;

    @NotNull // data que precisa devolver
    private LocalDate dataVencimento;

    // pode ser null se ainda não devolveu
    private LocalDate dataDevolucao;

    // contar as renovações
    private int renovacoes = 0;

}
