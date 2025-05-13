package com.example.demo.repository;

import com.example.demo.model.LivroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<LivroModel, Long>{
    Optional<LivroModel> findByLivroId(Long livroId);
    List<LivroModel> findByTituloContainingIgnoreCase(String titulo);
    List<LivroModel> findByAutorContainingIgnoreCase(String autor);
    List<LivroModel> findByGeneroContainingIgnoreCase(String genero);
    List<LivroModel> findByCursoContainingIgnoreCase(String curso);
    List<LivroModel> findByEditoraContainingIgnoreCase(String editora);

    List<LivroModel> findByTituloContainingIgnoreCaseAndAutorContainingIgnoreCaseAndGeneroContainingIgnoreCaseAndEditoraContainingIgnoreCaseAndCursoContainingIgnoreCase(
            String titulo, String autor, String genero, String editora, String curso);

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
