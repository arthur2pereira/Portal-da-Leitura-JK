package com.example.demo.dto;

import com.example.demo.model.PenalidadeModel;

import java.time.LocalDate;

public class PenalidadeDTO {
    private Long penalidadeId;
    private String matricula;
    private String motivo;
    private String tipo;
    private LocalDate dataAplicacao;
    private int diasBloqueio;

    public PenalidadeDTO(PenalidadeModel model) {
        this.penalidadeId = model.getPenalidadeId();
        this.matricula = model.getAluno().getMatricula();
        this.motivo = model.getMotivo();
        this.tipo = model.getTipo();
        this.dataAplicacao = model.getDataAplicacao();
        this.diasBloqueio = model.getDiasBloqueio();
    }

    public PenalidadeDTO(Long penalidadeId, String matricula, String motivo, String tipo, LocalDate dataAplicacao, int diasBloqueio) {
        this.penalidadeId = penalidadeId;
        this.matricula = matricula;
        this.motivo = motivo;
        this.tipo = tipo;
        this.dataAplicacao = dataAplicacao;
        this.diasBloqueio = diasBloqueio;
    }

    public PenalidadeDTO() {
    }

    public Long getPenalidadeId() {
        return penalidadeId;
    }

    public void setPenalidadeId(Long penalidadeId) {
        this.penalidadeId = penalidadeId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public int getDiasBloqueio() {
        return diasBloqueio;
    }

    public void setDiasBloqueio(int diasBloqueio) {
        this.diasBloqueio = diasBloqueio;
    }
}

