package com.example.demo.repository;

import com.example.demo.model.PenalidadeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenalidadeRepository extends JpaRepository<PenalidadeModel, Long> {
    List<PenalidadeModel> findByAlunoMatricula(String matricula);
    List<PenalidadeModel> findByTipoIgnoreCase(String tipo);
}
