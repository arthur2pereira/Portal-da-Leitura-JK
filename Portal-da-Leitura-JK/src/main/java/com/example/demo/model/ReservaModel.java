package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "reservas")
public class ReservaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservaId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula_aluno", referencedColumnName = "matricula")
    private AlunoModel aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id")
    private LivroModel livro;

    private Boolean status = true;

    private LocalDate dataReserva;

    private LocalDate dataVencimento;

    public ReservaModel(Long reservaId, AlunoModel aluno, LivroModel livro, Boolean status, LocalDate dataReserva, LocalDate dataVencimento) {
        this.reservaId = reservaId;
        this.aluno = aluno;
        this.livro = livro;
        this.status = status;
        this.dataReserva = dataReserva;
        this.dataVencimento = dataVencimento;
    }

    public ReservaModel() {
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public AlunoModel getAluno() {
        return aluno;
    }

    public void setAluno(AlunoModel aluno) {
        this.aluno = aluno;
    }

    public LivroModel getLivro() {
        return livro;
    }

    public void setLivro(LivroModel livro) {
        this.livro = livro;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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

