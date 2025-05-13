package com.example.demo.dto;

import com.example.demo.model.NotificacaoModel;
import lombok.Data;

@Data
public class NotificacaoDTO {
    private Long notificacaoId;
    private String matricula;
    private Long bibliotecarioId;
    private String mensagem;
    private String tipo;
    private boolean lida;

    public NotificacaoDTO(NotificacaoModel model) {
        this.notificacaoId = model.getNotificacaoId();
        this.matricula = model.getAluno().getMatricula();
        this.bibliotecarioId = model.getBibliotecario().getBibliotecarioId();
        this.mensagem = model.getMensagem();
        this.tipo = model.getTipo();
        this.lida = model.isLida();
    }

    public NotificacaoDTO(Long notificacaoId, String matricula, Long bibliotecarioId, String mensagem, String tipo, boolean lida) {
        this.notificacaoId = notificacaoId;
        this.matricula = matricula;
        this.bibliotecarioId = bibliotecarioId;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.lida = lida;
    }

    public NotificacaoDTO() {
    }

    public Long getNotificacaoId() {
        return notificacaoId;
    }

    public void setNotificacaoId(Long notificacaoId) {
        this.notificacaoId = notificacaoId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Long getBibliotecarioId() {
        return bibliotecarioId;
    }

    public void setBibliotecarioId(Long bibliotecarioId) {
        this.bibliotecarioId = bibliotecarioId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }
}
