package com.example.demo.dto;

import com.example.demo.model.LivroModel;
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

    public LivroDTO(LivroModel model) {
        this.livroId = model.getLivroId();
        this.titulo = model.getTitulo();
        this.autor = model.getAutor();
        this.genero = model.getGenero();
        this.curso = model.getCurso();
        this.editora = model.getEditora();
        this.anoPublicacao = model.getAnoPublicacao();
        this.descricao = model.getDescricao();
        this.quantidade = model.getQuantidade();
    }
}

