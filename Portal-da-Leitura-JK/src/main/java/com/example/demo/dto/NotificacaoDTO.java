package com.example.demo.dto;

import java.time.LocalDate;

public class NotificacaoDTO {
    private Long id;
    private Long alunoId;
    private String mensagem;
    private LocalDate dataEnvio;

    public NotificacaoDTO(Long id, Long alunoId, String mensagem, LocalDate dataEnvio) {
        this.id = id;
        this.alunoId = alunoId;
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}

