package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDTO {
    private Long NotificacaoId;
    private String matricula;
    private String mensagem;
    private String tipo;
    private LocalDate dataEnvio;
    private Boolean lida;
}
