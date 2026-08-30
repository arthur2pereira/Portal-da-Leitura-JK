package com.example.demo.dto;

import com.example.demo.model.ReservaModel;

import java.time.LocalDate;

public class ReservaDTO {
    private Long reservaId;
    private String matricula;
    private String nome;
    private Long livroId;
    private String titulo;
    private boolean status;
    private LocalDate dataReserva;
    private LocalDate dataVencimento;

    public ReservaDTO(ReservaModel model) {
        this.reservaId = model.getReservaId();
        this.matricula = model.getAluno().getMatricula();
        this.nome = model.getAluno().getNome();
        this.livroId = model.getLivro().getLivroId();
        this.titulo = model.getLivro().getTitulo();
        this.status = model.getStatus();
        this.dataReserva = model.getDataReserva();
        this.dataVencimento = model.getDataVencimento();
    }

    public ReservaDTO(Long reservaId, String matricula,String nome, Long livroId, String titulo, boolean status, LocalDate dataReserva, LocalDate dataVencimento) {
        this.reservaId = reservaId;
        this.matricula = matricula;
        this.nome = nome;
        this.livroId = livroId;
        this.titulo = titulo;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

