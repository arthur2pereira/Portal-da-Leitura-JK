package com.example.demo.dto;

import com.example.demo.model.NotificacaoModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDTO {
    private Long notificacaoId;
    private String matricula;
    private String mensagem;
    private String tipo;
    private boolean lida;

    public NotificacaoDTO(NotificacaoModel model) {
        this.notificacaoId = model.getNotificacaoId();
        this.matricula = model.getAluno().getMatricula();
        this.mensagem = model.getMensagem();
        this.tipo = model.getTipo();
        this.lida = model.isLida();
    }
}
