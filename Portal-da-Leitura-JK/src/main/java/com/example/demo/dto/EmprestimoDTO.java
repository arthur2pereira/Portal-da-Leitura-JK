package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDTO {
    private Long emprestimoId;
    private String matricula;
    private Long livroId;
    private String email;
    private LocalDate dataEmprestimo;
    private LocalDate dataVencimento;
    private LocalDate dataDevolucao;
    private int renovacoes;
    private String status;

}
