package com.example.demo.repository;

import com.example.demo.model.NotificacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoModel, Long> {

    List<NotificacaoModel> findByAlunoMatricula(String matricula);
    List<NotificacaoModel> findByTipo(String tipo);
    List<NotificacaoModel> findByTipoAndAlunoMatricula(String tipo, String matricula);
    List<NotificacaoModel> findByAlunoMatriculaAndLidaFalse(String matricula);
}
