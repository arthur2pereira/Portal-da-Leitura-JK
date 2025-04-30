package com.example.demo.dto;

import com.example.demo.model.ReservaModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    public ReservaDTO(Long reservaId, String matricula, Long livroId, boolean status, LocalDate dataReserva, LocalDate dataVencimento) {
        this.reservaId = reservaId;
        this.matricula = matricula;
        this.livroId = livroId;
        this.status = status;
        this.dataReserva = dataReserva;
        this.dataVencimento = dataVencimento;
    }

    public ReservaDTO() {
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
}

