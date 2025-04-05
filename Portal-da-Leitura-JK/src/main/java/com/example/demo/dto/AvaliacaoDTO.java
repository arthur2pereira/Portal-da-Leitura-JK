package com.example.demo.dto;

public class AvaliacaoDTO {
    private Long id;
    private Long matricula;
    private Long livroId;
    private int nota;
    private String comentario;

    public AvaliacaoDTO(Long id, Long matricula, Long livroId, int nota, String comentario) {
        this.id = id;
        this.matricula = matricula;
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

    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
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

