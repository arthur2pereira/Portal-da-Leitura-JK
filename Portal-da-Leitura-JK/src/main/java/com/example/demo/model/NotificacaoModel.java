package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Table(name = "notificacoes")
public class NotificacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificacaoId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_aluno", referencedColumnName = "matricula")
    private AlunoModel aluno;

    @NotBlank
    @Size(max = 300)
    private String mensagem;

    private String tipo;

    private boolean lida = false;

    public NotificacaoModel(Long notificacaoId, AlunoModel aluno, String mensagem, String tipo, boolean lida) {
        this.notificacaoId = notificacaoId;
        this.aluno = aluno;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.lida = lida;
    }

    public NotificacaoModel() {
    }

    public Long getNotificacaoId() {
        return notificacaoId;
    }

    public void setNotificacaoId(Long notificacaoId) {
        this.notificacaoId = notificacaoId;
    }

    public AlunoModel getAluno() {
        return aluno;
    }

    public void setAluno(AlunoModel aluno) {
        this.aluno = aluno;
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
