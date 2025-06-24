package com.example.demo.dto;

import com.example.demo.model.BibliotecarioModel;

public class BibliotecarioDTO {
    private Long bibliotecarioId;
    private String nome;
    private String email;
    private String senha;

    public BibliotecarioDTO(BibliotecarioModel model) {
        this.bibliotecarioId = model.getBibliotecarioId();
        this.nome = model.getNome();
        this.email = model.getEmail();
        this.senha = model.getSenha();
    }

    public BibliotecarioDTO(Long bibliotecarioId, String nome, String email, String senha) {
        this.bibliotecarioId = bibliotecarioId;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public BibliotecarioDTO(Long bibliotecarioId, String nome, String email) {
        this.bibliotecarioId = bibliotecarioId;
        this.nome = nome;
        this.email = email;
    }

    public BibliotecarioDTO() {
    }

    public Long getBibliotecarioId() {
        return bibliotecarioId;
    }

    public void setBibliotecarioId(Long bibliotecarioId) {
        this.bibliotecarioId = bibliotecarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

