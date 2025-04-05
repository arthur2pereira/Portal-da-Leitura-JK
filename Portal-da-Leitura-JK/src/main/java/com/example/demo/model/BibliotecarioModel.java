package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Adicionado para validações
import lombok.*;

@Entity
@Data // Gera automaticamente getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor // Gera construtor com todos os campos
@Table(name = "bibliotecarios") // Mapeia para a tabela "bibliotecarios"
public class BibliotecarioModel {

    @Id // Define o campo como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long id;

    @NotBlank // Não pode ser nulo ou vazio
    @Size(max = 100) // Nome com no máximo 100 caracteres
    private String nome;

    @NotBlank
    @Email // Validação de e-mail
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 6) // Pelo menos 6 caracteres na senha
    private String senha;
}
