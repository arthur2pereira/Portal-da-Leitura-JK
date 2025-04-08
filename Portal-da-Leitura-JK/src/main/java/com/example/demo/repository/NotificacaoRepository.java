package com.example.demo.repository;

import com.example.demo.model.NotificacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoModel, Long> {

    // Consultar notificações por matrícula de aluno
    List<NotificacaoModel> findByAlunoMatricula(String matricula);

    // Consultar notificações por tipo
    List<NotificacaoModel> findByTipo(String tipo);

    // Consultar notificações por tipo e matrícula
    List<NotificacaoModel> findByTipoAndAlunoMatricula(String tipo, String matricula);

    // Consultar notificações não lidas por aluno
    List<NotificacaoModel> findByAlunoMatriculaAndLidaFalse(String matricula);

    // Consultar notificações por data de envio (ex: depois de certa data)
    List<NotificacaoModel> findByDataEnvioAfterAndAlunoMatricula(LocalDate data, String matricula);

    List<NotificacaoModel> findByAlunoMatriculaOrderByDataEnvioDesc(String matricula);


}
