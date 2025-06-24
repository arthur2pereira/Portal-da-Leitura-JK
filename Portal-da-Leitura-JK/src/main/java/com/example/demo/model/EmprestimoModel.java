package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
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

    @NotNull
    private int renovacoes = 0;

    @NotNull
    private String status;

    public EmprestimoModel(Long emprestimoId, AlunoModel aluno, LivroModel livro, BibliotecarioModel bibliotecario, LocalDate dataEmprestimo, LocalDate dataVencimento, LocalDate dataDevolucao, int renovacoes, String status) {
        this.emprestimoId = emprestimoId;
        this.aluno = aluno;
        this.livro = livro;
        this.bibliotecario = bibliotecario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataVencimento = dataVencimento;
        this.dataDevolucao = dataDevolucao;
        this.renovacoes = renovacoes;
        this.status = status;
    }

    public EmprestimoModel() {
    }

    public Long getEmprestimoId() {
        return emprestimoId;
    }

    public void setEmprestimoId(Long emprestimoId) {
        this.emprestimoId = emprestimoId;
    }

    public AlunoModel getAluno() {
        return aluno;
    }

    public void setAluno(AlunoModel aluno) {
        this.aluno = aluno;
    }

    public LivroModel getLivro() {
        return livro;
    }

    public void setLivro(LivroModel livro) {
        this.livro = livro;
    }

    public BibliotecarioModel getBibliotecario() {
        return bibliotecario;
    }

    public void setBibliotecario(BibliotecarioModel bibliotecario) {
        this.bibliotecario = bibliotecario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public int getRenovacoes() {
        return renovacoes;
    }

    public void setRenovacoes(int renovacoes) {
        this.renovacoes = renovacoes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

