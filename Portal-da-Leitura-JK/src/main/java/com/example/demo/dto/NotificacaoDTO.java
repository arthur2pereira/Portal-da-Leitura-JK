package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDTO {
    private Long id;
    private String matricula;
    private String mensagem;
    private LocalDate dataEnvio;
}
