package com.example.demo.dto;

import com.example.demo.model.LivroModel;

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

    public LivroDTO(Long livroId, String titulo, String autor, String genero, String curso, String editora, int anoPublicacao, String descricao, Integer quantidade) {
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

    public LivroDTO() {
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

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
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

