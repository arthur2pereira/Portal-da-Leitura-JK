package com.example.demo.integration.dto;

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

    public BooksDTO(Long booksId, String titulo, String autor, String genero, String descricao, Integer anoPublicacao, String curso, String editora, Integer quantidade) {
        BooksId = booksId;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.descricao = descricao;
        this.anoPublicacao = anoPublicacao;
        this.curso = curso;
        this.editora = editora;
        this.quantidade = quantidade;
    }

    public BooksDTO() {
    }

    public Long getBooksId() {
        return BooksId;
    }

    public void setBooksId(Long booksId) {
        BooksId = booksId;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
