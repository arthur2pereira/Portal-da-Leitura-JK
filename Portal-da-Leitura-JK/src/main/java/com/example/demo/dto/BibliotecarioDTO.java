package com.example.demo.dto;

import com.example.demo.model.BibliotecarioModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
}

