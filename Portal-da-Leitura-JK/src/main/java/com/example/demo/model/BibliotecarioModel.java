package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Table(name = "bibliotecarios")
public class BibliotecarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bibliotecarioId;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String senha;

    public BibliotecarioModel(Long bibliotecarioId, String nome, String email, String senha) {
        this.bibliotecarioId = bibliotecarioId;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public BibliotecarioModel() {
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
