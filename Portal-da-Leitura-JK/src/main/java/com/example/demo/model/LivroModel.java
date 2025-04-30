package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Table(name = "livros")
public class LivroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "livro_id")
    private Long livroId;

    @NotBlank
    @Size(max = 150)
    private String titulo;

    @NotBlank
    @Size(max = 100)
    private String autor;

    @NotBlank
    @Size(max = 50)
    private String genero;

    @Size(max = 100)
    private String curso;

    @NotBlank
    @Column(nullable = false)
    private String editora;

    @Min(1500)
    @Max(2100)
    @NotBlank
    private Integer anoPublicacao;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Min(0)
    @NotBlank
    private Integer quantidade = 1;

    public LivroModel(Long livroId, String titulo, String autor, String genero, String curso, String editora, Integer anoPublicacao, String descricao, Integer quantidade) {
        this.livroId = livroId;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.curso = curso;
        this.editora = editora;
        this.anoPublicacao = anoPublicacao;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public LivroModel() {
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
