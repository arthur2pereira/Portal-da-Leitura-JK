package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livros")
public class LivroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String autor;

    private String genero;

    private String curso; // Pode ser nulo

    private Integer anoPublicacao;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private Integer quantidade = 1;
}
