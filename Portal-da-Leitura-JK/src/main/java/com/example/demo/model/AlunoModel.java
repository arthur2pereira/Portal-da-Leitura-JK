package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Table(name = "alunos")
public class AlunoModel {

    @Id
    @NotBlank
    @Pattern(regexp = "\\d{13}", message = "A matrícula deve conter exatamente 13 dígitos numéricos.")
    @Column(length = 13, unique = true)
    private String matricula;

    @NotBlank(message = "O nome é obrigatório.")
    @Column(length = 100)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    @Column(unique = true, length = 100)
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;


    private Boolean status = true;

    public AlunoModel(String matricula, String nome, String email, String senha, Boolean status) {
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.status = status;
    }

    public AlunoModel() {
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
