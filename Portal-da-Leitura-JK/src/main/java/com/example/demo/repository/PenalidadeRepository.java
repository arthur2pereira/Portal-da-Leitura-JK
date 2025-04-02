package com.example.demo.repository;

import com.example.demo.model.PenalidadeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenalidadeRepository extends JpaRepository<PenalidadeModel, Long> {
    // Consultar penalidades por aluno
    List<PenalidadeModel> findByAlunoId(Long alunoId);

    // Consultar penalidades por tipo (ex: atraso, comentário ofensivo)
    List<PenalidadeModel> findByTipo(String tipo);
}
