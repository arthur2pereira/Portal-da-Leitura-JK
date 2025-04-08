package com.example.demo.dto;

import java.time.LocalDate;

public class ReservaDTO {
    private Long id;
    private String matricula;
    private Long livroId;
    private LocalDate dataReserva;
    private LocalDate dataVencimento;

    public ReservaDTO(Long id, String matricula, Long livroId, LocalDate dataReserva, LocalDate dataVencimento) {
        this.id = id;
        this.matricula = matricula;
        this.livroId = livroId;
        this.dataReserva = dataReserva;
        this.dataVencimento = dataVencimento;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

