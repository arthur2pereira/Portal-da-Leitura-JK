package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notificacoes")
public class NotificacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aluno que vai receber a notificação
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", referencedColumnName = "matricula")
    private AlunoModel aluno;

    // Mensagem da notificação
    @NotBlank
    @Size(max = 300)
    private String mensagem;

    // Tipo da notificação (ex: "reserva", "atraso", "devolucao", etc.)
    @NotBlank
    @Size(max = 30)
    private String tipo;

    // Data em que a notificação foi enviada
    @PastOrPresent
    private LocalDate dataEnvio;

    // Se o aluno já visualizou a notificação
    private Boolean lida = false;
}
