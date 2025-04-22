package com.example.demo.repository;

import com.example.demo.model.AvaliacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoModel, Long> {
    List<AvaliacaoModel> findByLivroLivroId(Long livroId);
    List<AvaliacaoModel> findByAlunoMatricula(String matricula);
    Optional<AvaliacaoModel> findByAvaliacaoId(Long avalicacaoId);
}
