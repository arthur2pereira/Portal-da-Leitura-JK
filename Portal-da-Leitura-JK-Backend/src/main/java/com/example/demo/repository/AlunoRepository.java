package com.example.demo.repository;

import com.example.demo.model.AlunoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoModel, Long> {

    List<AlunoModel> findByNomeContainingIgnoreCase(String nome);
    Optional<AlunoModel> findByMatricula(String matricula);
}

