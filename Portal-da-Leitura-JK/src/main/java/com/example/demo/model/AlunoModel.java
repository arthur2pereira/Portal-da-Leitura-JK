package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alunos")
public class AlunoModel {

    @Id
    @NotBlank(message = "A matrícula é obrigatória.")
    @Pattern(regexp = "\\d{13}", message = "A matrícula deve conter exatamente 13 dígitos numéricos.")
    @Column(length = 13, unique = true) // garante unicidade e define tamanho fixo no banco
    private Long matricula;

    @NotBlank(message = "O nome é obrigatório.")
    @Column(length = 100)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    @Column(unique = true, length = 100)
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @JsonIgnore // ignora a senha no JSON de retorno (segurança)
    private String senha;

    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate dataNascimento;

    private Boolean status = true; // ativo por padrão
}
