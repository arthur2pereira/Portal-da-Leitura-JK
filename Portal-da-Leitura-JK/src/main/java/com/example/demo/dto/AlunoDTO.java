package com.example.demo.dto;

import java.time.LocalDate;

public class AlunoDTO {
    private Long matricula;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String status;

    public AlunoDTO(Long matricula, String nome, String email, LocalDate dataNascimento, String status) {
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.status = status;
    }

    // Getters e Setters
    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
