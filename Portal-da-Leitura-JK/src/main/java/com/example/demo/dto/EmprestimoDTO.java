package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    private Long bibliotecarioId;
    private LocalDate dataEmprestimo;
    private LocalDate dataVencimento;
    private LocalDate dataDevolucao;

    public EmprestimoDTO(Long emprestimoId, @NotNull LocalDate dataEmprestimo, @NotNull LocalDate dataVencimento, LocalDate dataDevolucao, @NotBlank @Size(max = 150) String titulo, @NotBlank @Pattern(regexp = "\\d{13}", message = "A matrícula deve conter exatamente 13 dígitos numéricos.") String matricula) {

    }
}
