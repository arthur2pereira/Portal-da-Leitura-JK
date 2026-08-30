package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contas_inativas")
public class ContasInativasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contas")
    private Long contaId;

    @OneToOne
    @JoinColumn(name = "matricula", referencedColumnName = "matricula", nullable = false, unique = true)
    private AlunoModel aluno;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 100, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Motivo motivo;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_exclusao_final")
    private LocalDateTime dataExclusaoFinal;

    public enum Motivo {
        DESATIVACAO,
        EXCLUSAO
    }

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public AlunoModel getAluno() {
        return aluno;
    }

    public void setAluno(AlunoModel aluno) {
        this.aluno = aluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDateTime getDataExclusaoFinal() {
        return dataExclusaoFinal;
    }

    public void setDataExclusaoFinal(LocalDateTime dataExclusaoFinal) {
        this.dataExclusaoFinal = dataExclusaoFinal;
    }
}
