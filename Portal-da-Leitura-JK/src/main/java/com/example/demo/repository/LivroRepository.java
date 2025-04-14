package com.example.demo.repository;

import com.example.demo.model.LivroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<LivroModel, Long> {
    List<LivroModel> findByTituloContainingIgnoreCase(String titulo);
    List<LivroModel> findByAutorContainingIgnoreCase(String autor);
    List<LivroModel> findByGeneroContainingIgnoreCase(String genero);
    List<LivroModel> findByCursoContainingIgnoreCase(String curso);
    boolean existsByTituloAndAutor(String titulo, String autor);
    Optional<LivroModel> findByLivroId(Long livroId);
}
