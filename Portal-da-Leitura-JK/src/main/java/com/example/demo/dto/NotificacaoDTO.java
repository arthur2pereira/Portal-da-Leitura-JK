package com.example.demo.dto;

import java.time.LocalDate;

public class NotificacaoDTO {
    private Long id;
    private Long matricula;
    private String mensagem;
    private LocalDate dataEnvio;

    public NotificacaoDTO(Long id, Long matricula, String mensagem, LocalDate dataEnvio) {
        this.id = id;
        this.matricula = matricula;
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

    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
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

