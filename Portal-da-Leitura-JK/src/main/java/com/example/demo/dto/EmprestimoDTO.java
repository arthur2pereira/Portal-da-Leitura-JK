package com.example.demo.dto;

import com.example.demo.model.EmprestimoModel;

import java.time.LocalDate;

public class EmprestimoDTO {
    private Long emprestimoId;
    private String matricula;
    private Long livroId;
    private Long bibliotecarioId;
    private LocalDate dataEmprestimo;
    private LocalDate dataVencimento;
    private LocalDate dataDevolucao;
    private int renovacoes;
    private String status;

    public EmprestimoDTO(EmprestimoModel model) {
        this.emprestimoId = model.getEmprestimoId();
        this.matricula = model.getAluno().getMatricula();
        this.livroId = model.getLivro().getLivroId();
        this.bibliotecarioId = model.getBibliotecario().getBibliotecarioId();
        this.dataEmprestimo = model.getDataEmprestimo();
        this.dataVencimento = model.getDataVencimento();
        this.dataDevolucao = model.getDataDevolucao();
        this.renovacoes = model.getRenovacoes();
        this.status = model.getStatus();
    }

    public EmprestimoDTO(Long emprestimoId, String matricula, Long livroId, Long bibliotecarioId, LocalDate dataEmprestimo, LocalDate dataVencimento, LocalDate dataDevolucao, int renovacoes, String status) {
        this.emprestimoId = emprestimoId;
        this.matricula = matricula;
        this.livroId = livroId;
        this.bibliotecarioId = bibliotecarioId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataVencimento = dataVencimento;
        this.dataDevolucao = dataDevolucao;
        this.renovacoes = renovacoes;
        this.status = status;
    }

    public EmprestimoDTO() {
    }

    public Long getEmprestimoId() {
        return emprestimoId;
    }

    public void setEmprestimoId(Long emprestimoId) {
        this.emprestimoId = emprestimoId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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
