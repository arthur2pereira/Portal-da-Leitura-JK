package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Table(name = "avaliacoes")
public class AvaliacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avaliacaoId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula_aluno", referencedColumnName = "matricula")
    private AlunoModel aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id")
    private LivroModel livro;

    @NotNull
    @Min(0)
    @Max(5)
    private Integer nota;

    @Size(max = 500)
    private String comentario;

    public AvaliacaoModel(Long avaliacaoId, AlunoModel aluno, LivroModel livro, Integer nota, String comentario) {
        this.avaliacaoId = avaliacaoId;
        this.aluno = aluno;
        this.livro = livro;
        this.nota = nota;
        this.comentario = comentario;
    }

    public AvaliacaoModel() {
    }

    public Long getAvaliacaoId() {
        return avaliacaoId;
    }

    public void setAvaliacaoId(Long avaliacaoId) {
        this.avaliacaoId = avaliacaoId;
    }

    public AlunoModel getAluno() {
        return aluno;
    }

    public void setAluno(AlunoModel aluno) {
        this.aluno = aluno;
    }

    public LivroModel getLivro() {
        return livro;
    }

    public void setLivro(LivroModel livro) {
        this.livro = livro;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}