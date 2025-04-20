package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ReservaDTO {
    private Long reservaId;
    private String matricula;
    private Long livroId;
    private boolean status;
    private LocalDate dataReserva;
    private LocalDate dataVencimento;

    public ReservaDTO(Long reservaId, LocalDate dataReserva, LocalDate dataVencimento, @NotBlank @Size(max = 150) String titulo, @NotBlank @Pattern(regexp = "\\d{13}", message = "A matrícula deve conter exatamente 13 dígitos numéricos.") String matricula) {
    }
}

