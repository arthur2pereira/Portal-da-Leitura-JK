package com.example.demo.repository;

import com.example.demo.model.LivroModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<LivroModel, Long>, JpaSpecificationExecutor<LivroModel> {
    Optional<LivroModel> findByLivroId(Long livroId);
    Page<LivroModel> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    boolean existsByTituloAndAutor(String titulo, String autor);
    @Query("SELECT DISTINCT l.curso FROM LivroModel l")
    List<String> findAllCursosUnicos();
    @Query("SELECT DISTINCT l.autor FROM LivroModel l")
    List<String> findAllAutoresUnicos();
    @Query("SELECT DISTINCT l.editora FROM LivroModel l")
    List<String> findAllEditorasUnicos();
    @Query("SELECT DISTINCT l.genero FROM LivroModel l")
    List<String> findAllGenerosUnicos();

}
