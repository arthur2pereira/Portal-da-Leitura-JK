package com.example.demo.repository;

import com.example.demo.model.NotificacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoModel, Long> {

    List<NotificacaoModel> findByAlunoMatricula(String matricula);
    Optional<NotificacaoModel> findByNotificacaoId(Long Notificacaoid);
    List<NotificacaoModel> findByAlunoMatriculaAndLidaFalse(String matricula);
}
