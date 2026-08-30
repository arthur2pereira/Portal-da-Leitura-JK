package com.example.demo.repository;

import com.example.demo.model.ContasInativasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContasInativasRepository extends JpaRepository<ContasInativasModel, Long> {

    Optional<ContasInativasModel> findByAluno_Matricula(String matricula);
    List<ContasInativasModel> findByMotivo(ContasInativasModel.Motivo motivo);
    List<ContasInativasModel> findByDataExclusaoFinalBefore(LocalDateTime data);
}
