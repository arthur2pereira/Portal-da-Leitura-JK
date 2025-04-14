package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PenalidadeDTO {
    private Long penalidadeId;
    private String matricula;
    private String tipo;
    private LocalDate dataAplicacao;
    private int diasBloqueio;
}

