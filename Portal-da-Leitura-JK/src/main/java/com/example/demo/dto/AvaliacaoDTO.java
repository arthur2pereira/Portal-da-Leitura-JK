package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoDTO {
    private Long avaliacao_id;
    private String matricula;
    private Long livro_id;
    private int nota;
    private String comentario;
}

