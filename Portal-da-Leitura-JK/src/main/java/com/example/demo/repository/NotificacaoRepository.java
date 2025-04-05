package com.example.demo.repository;

import com.example.demo.model.NotificacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoModel, Long> {

    // Consultar notificações por matrícula de aluno
    List<NotificacaoModel> findByMatricula(Long matricula);

    // Consultar notificações por tipo (ex: alerta, penalidade)
    List<NotificacaoModel> findByTipo(String tipo);

    // Consultar notificações por tipo e matrícula (ex: alerta para um aluno específico)
    List<NotificacaoModel> findByTipoAndMatricula(String tipo, Long matricula);

    // Consultar notificações não lidas por aluno (adicionar campo 'lida' no modelo)
    List<NotificacaoModel> findByMatriculaAndLidaFalse(Long matricula);

    // Consultar notificações por data de criação (adicionar 'dataCriacao' no modelo)
    List<NotificacaoModel> findByDataCriacaoAfterAndMatricula(String data, Long matricula);

    // Consultar uma notificação por id (exemplo para detalhes)
    Optional<NotificacaoModel> findById(Long id);
}
