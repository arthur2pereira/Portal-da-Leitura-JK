package com.example.demo.dto;

import com.example.demo.model.PenalidadeModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PenalidadeDTO {
    private Long penalidadeId;
    private String matricula;
    private String motivo;
    private String tipo;
    private LocalDate dataAplicacao;
    private int diasBloqueio;

    public PenalidadeDTO(PenalidadeModel model) {
        this.penalidadeId = model.getPenalidadeId();
        this.matricula = model.getAluno().getMatricula();
        this.motivo = model.getMotivo();
        this.tipo = model.getTipo();
        this.dataAplicacao = model.getDataAplicacao();
        this.diasBloqueio = model.getDiasBloqueio();
    }
}

