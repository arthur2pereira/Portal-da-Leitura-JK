package com.example.demo.dto;

import com.example.demo.model.AlunoModel;

public class AlunoDTO {
    private String matricula;
    private String nome;
    private String email;
    private String senha;
    private Boolean status;

    public AlunoDTO(AlunoModel model) {
        this.matricula = model.getMatricula();
        this.nome = model.getNome();
        this.email = model.getEmail();
        this.senha = model.getSenha();
    }

    public AlunoDTO(String matricula, String nome, String email, String senha, Boolean status) {
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.status = status;
    }

    public AlunoDTO() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
