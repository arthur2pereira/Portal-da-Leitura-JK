package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "penalidades")
public class PenalidadeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long penalidadeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula_aluno", referencedColumnName = "matricula")
    private AlunoModel aluno;

    private String motivo;

    @Column(name = "tipo_penalidade", nullable = false)
    private String tipo;

    @PastOrPresent(message = "A data de aplicação não pode ser no futuro.")
    private LocalDate dataAplicacao;

    private Integer diasBloqueio;

    public PenalidadeModel(Long penalidadeId, AlunoModel aluno, String motivo, String tipo, LocalDate dataAplicacao, Integer diasBloqueio) {
        this.penalidadeId = penalidadeId;
        this.aluno = aluno;
        this.motivo = motivo;
        this.tipo = tipo;
        this.dataAplicacao = dataAplicacao;
        this.diasBloqueio = diasBloqueio;
    }

    public PenalidadeModel() {
    }

    public Long getPenalidadeId() {
        return penalidadeId;
    }

    public void setPenalidadeId(Long penalidadeId) {
        this.penalidadeId = penalidadeId;
    }

    public AlunoModel getAluno() {
        return aluno;
    }

    public void setAluno(AlunoModel aluno) {
        this.aluno = aluno;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public Integer getDiasBloqueio() {
        return diasBloqueio;
    }

    public void setDiasBloqueio(Integer diasBloqueio) {
        this.diasBloqueio = diasBloqueio;
    }
}
