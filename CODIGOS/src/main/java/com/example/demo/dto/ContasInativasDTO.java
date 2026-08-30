package com.example.demo.dto;

import com.example.demo.model.ContasInativasModel;
import java.time.LocalDateTime;

public class ContasInativasDTO {

    private Long contaId;
    private String matricula;
    private String nome;
    private String email;
    private String motivo;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataExclusaoFinal;

    public ContasInativasDTO() {}

    public ContasInativasDTO(ContasInativasModel model) {
        this.contaId = model.getContaId();
        this.matricula = model.getAluno().getMatricula();
        this.nome = model.getNome();
        this.email = model.getEmail();
        this.motivo = model.getMotivo().name();
        this.dataSolicitacao = model.getDataSolicitacao();
        this.dataExclusaoFinal = model.getDataExclusaoFinal();
    }

    // getters e setters
    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
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
