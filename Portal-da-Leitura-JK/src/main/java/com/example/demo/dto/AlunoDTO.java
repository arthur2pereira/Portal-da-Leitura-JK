package com.example.demo.dto;

import lombok.AllArgsConstructor;
import com.example.demo.model.AlunoModel;
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

    public AlunoDTO(AlunoModel model) {
        this.matricula = model.getMatricula();
        this.nome = model.getNome();
        this.email = model.getEmail();
        this.senha = model.getSenha();
    }
}
