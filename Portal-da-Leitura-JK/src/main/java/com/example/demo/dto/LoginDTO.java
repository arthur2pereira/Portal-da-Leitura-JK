package com.example.demo.dto;

public class LoginDTO {
    private String matricula;
    private String senha;

    public LoginDTO() {
    }

    public LoginDTO(String matricula, String senha) {
        this.matricula = matricula;
        this.senha = senha;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
