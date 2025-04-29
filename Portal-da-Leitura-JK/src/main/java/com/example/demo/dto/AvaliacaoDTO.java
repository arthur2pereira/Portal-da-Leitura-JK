package com.example.demo.dto;

import com.example.demo.model.AvaliacaoModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoDTO {
    private Long avaliacaoId;
    private String matricula;
    private Long livroId;
    private int nota;
    private String comentario;

    public AvaliacaoDTO(AvaliacaoModel model) {
        this.avaliacaoId = model.getAvaliacaoId();
        this.matricula = model.getAluno().getMatricula();
        this.livroId = model.getLivro().getLivroId();
        this.nota = model.getNota();
        this.comentario = model.getComentario();
    }
}

