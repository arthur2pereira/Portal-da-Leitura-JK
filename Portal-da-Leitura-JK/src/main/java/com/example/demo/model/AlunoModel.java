package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alunos")
public class AlunoModel {

    @Id
    private String matricula;

    private String nome;

    private String email;

    private String senha;

    private LocalDate dataNascimento;

    private Boolean status;
}

