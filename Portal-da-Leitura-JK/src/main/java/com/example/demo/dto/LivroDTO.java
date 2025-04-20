package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LivroDTO {
    private Long livroId;
    private String titulo;
    private String autor;
    private String genero;
    private String curso;
    private String editora;
    private int anoPublicacao;
    private String descricao;
    private Integer quantidade;
}

