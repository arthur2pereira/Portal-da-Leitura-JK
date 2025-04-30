package com.example.demo.dto;

import com.example.demo.model.AvaliacaoModel;

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

    public AvaliacaoDTO(Long avaliacaoId, String matricula, Long livroId, int nota, String comentario) {
        this.avaliacaoId = avaliacaoId;
        this.matricula = matricula;
        this.livroId = livroId;
        this.nota = nota;
        this.comentario = comentario;
    }

    public AvaliacaoDTO() {
    }

    public Long getAvaliacaoId() {
        return avaliacaoId;
    }

    public void setAvaliacaoId(Long avaliacaoId) {
        this.avaliacaoId = avaliacaoId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

