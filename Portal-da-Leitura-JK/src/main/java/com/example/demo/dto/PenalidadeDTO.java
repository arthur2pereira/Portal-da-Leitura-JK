package com.example.demo.dto;

import java.time.LocalDate;

public class PenalidadeDTO {
    private Long id;
    private Long alunoId;
    private String tipoPenalidade;
    private LocalDate dataAplicacao;
    private int diasBloqueado;

    public PenalidadeDTO(Long id, Long alunoId, String tipoPenalidade, LocalDate dataAplicacao, int diasBloqueado) {
        this.id = id;
        this.alunoId = alunoId;
        this.tipoPenalidade = tipoPenalidade;
        this.dataAplicacao = dataAplicacao;
        this.diasBloqueado = diasBloqueado;
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

    public String getTipoPenalidade() {
        return tipoPenalidade;
    }

    public void setTipoPenalidade(String tipoPenalidade) {
        this.tipoPenalidade = tipoPenalidade;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public int getDiasBloqueado() {
        return diasBloqueado;
    }

    public void setDiasBloqueado(int diasBloqueado) {
        this.diasBloqueado = diasBloqueado;
    }
}

