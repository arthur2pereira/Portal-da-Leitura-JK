package com.example.demo.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BooksDTO {
    private Long BooksId;
    private String titulo;
    private String autor;
    private String genero;
    private String descricao;
    private Integer anoPublicacao;
    private String curso;
    private String editora;
    private Integer quantidade;

    public BooksDTO(String titulo, String autor, String genero, String descricao, String curso, String editora) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.descricao = descricao;
        this.curso = curso;
        this.editora = editora;
    }
}
