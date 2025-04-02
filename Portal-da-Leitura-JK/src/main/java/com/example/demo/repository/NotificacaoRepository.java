package com.example.demo.repository;

import com.example.demo.model.NotificacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoModel, Long> {
    // Consultar notificações por aluno
    List<NotificacaoModel> findByAlunoId(Long alunoId);

    // Consultar notificações por tipo (ex: alerta, penalidade)
    List<NotificacaoModel> findByTipo(String tipo);
}
