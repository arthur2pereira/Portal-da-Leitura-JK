package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "penalidades")
public class PenalidadeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long penalidadeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula_aluno", referencedColumnName = "matricula")
    private AlunoModel aluno;

    private String motivo;

    private Integer diasBloqueio;

    @PastOrPresent(message = "A data de aplicação não pode ser no futuro.")
    private LocalDate dataAplicacao;

    @Column(name = "tipo_penalidade", nullable = false)
    private String tipo;
}
