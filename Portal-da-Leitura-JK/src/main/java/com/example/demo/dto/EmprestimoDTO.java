package com.example.demo.dto;

import com.example.demo.model.EmprestimoModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDTO {
    private Long emprestimoId;
    private String matricula;
    private Long livroId;
    private Long bibliotecarioId;
    private LocalDate dataEmprestimo;
    private LocalDate dataVencimento;
    private LocalDate dataDevolucao;
    private int renovacoes;
    private String status;

    public EmprestimoDTO(EmprestimoModel model) {
        this.emprestimoId = model.getEmprestimoId();
        this.matricula = model.getAluno().getMatricula();
        this.livroId = model.getLivro().getLivroId();
        this.bibliotecarioId = model.getBibliotecario().getBibliotecarioId();
        this.dataEmprestimo = model.getDataEmprestimo();
        this.dataVencimento = model.getDataVencimento();
        this.dataDevolucao = model.getDataDevolucao();
        this.renovacoes = model.getRenovacoes();
        this.status = model.getStatus();
    }
}
