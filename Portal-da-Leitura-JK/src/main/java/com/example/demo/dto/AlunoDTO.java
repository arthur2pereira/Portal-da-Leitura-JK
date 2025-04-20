package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AlunoDTO {
    private String matricula;
    private String nome;
    private String email;
    private String senha;
    private Boolean status;
}
