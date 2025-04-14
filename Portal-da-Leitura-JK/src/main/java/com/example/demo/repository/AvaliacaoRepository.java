package com.example.demo.repository;

import com.example.demo.model.AvaliacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoModel, Long> {
    List<AvaliacaoModel> findByLivro_LivroId(Long livroId);
    List<AvaliacaoModel> findByAluno_Matricula(String matricula);
}
