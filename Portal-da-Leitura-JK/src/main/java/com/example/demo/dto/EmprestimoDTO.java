package com.example.demo.dto;

import java.time.LocalDate;

public class EmprestimoDTO {
    private Long id;
    private Long alunoId;
    private Long livroId;
    private Long bibliotecarioId;
    private LocalDate dataEmprestimo;
    private LocalDate dataVencimento;
    private LocalDate dataDevolucao;

    public EmprestimoDTO(Long id, Long alunoId, Long livroId, Long bibliotecarioId,
                         LocalDate dataEmprestimo, LocalDate dataVencimento, LocalDate dataDevolucao) {
        this.id = id;
        this.alunoId = alunoId;
        this.livroId = livroId;
        this.bibliotecarioId = bibliotecarioId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataVencimento = dataVencimento;
        this.dataDevolucao = dataDevolucao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public Long getBibliotecarioId() {
        return bibliotecarioId;
    }

    public void setBibliotecarioId(Long bibliotecarioId) {
        this.bibliotecarioId = bibliotecarioId;
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
}
