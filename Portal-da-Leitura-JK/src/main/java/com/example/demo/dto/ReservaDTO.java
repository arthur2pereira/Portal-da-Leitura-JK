package com.example.demo.dto;

import com.example.demo.model.ReservaModel;

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

    public ReservaDTO(ReservaModel model) {
        this.reservaId = model.getReservaId();
        this.matricula = model.getAluno().getMatricula();
        this.livroId = model.getLivro().getLivroId();
        this.status = model.getStatus();
        this.dataReserva = model.getDataReserva();
        this.dataVencimento = model.getDataVencimento();
    }
}

