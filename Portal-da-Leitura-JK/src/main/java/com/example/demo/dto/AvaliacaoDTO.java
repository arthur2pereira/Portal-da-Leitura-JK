package com.example.demo.dto;

public class AvaliacaoDTO {
    private Long id;
    private Long alunoId;
    private Long livroId;
    private int nota;
    private String comentario;

    public AvaliacaoDTO(Long id, Long alunoId, Long livroId, int nota, String comentario) {
        this.id = id;
        this.alunoId = alunoId;
        this.livroId = livroId;
        this.nota = nota;
        this.comentario = comentario;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
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

