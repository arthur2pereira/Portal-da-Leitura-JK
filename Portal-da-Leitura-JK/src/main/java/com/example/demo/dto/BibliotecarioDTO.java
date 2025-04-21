package com.example.demo.dto;

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
}

