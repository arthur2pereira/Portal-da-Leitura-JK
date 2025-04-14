package com.example.demo.dto;

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
}

