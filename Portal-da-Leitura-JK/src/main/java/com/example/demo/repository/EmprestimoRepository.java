package com.example.demo.repository;

import com.example.demo.model.EmprestimoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<EmprestimoModel, Long> {

    List<EmprestimoModel> findByAlunoMatricula(String matricula);
    List<EmprestimoModel> findByLivroLivroId(Long livroId);
    List<EmprestimoModel> findByDataVencimentoBeforeAndDataDevolucaoIsNull(LocalDate data);
    List<EmprestimoModel> findByBibliotecarioBibliotecarioId(Long bibliotecarioId);
    boolean existsByAlunoMatriculaAndLivro_LivroId(String matricula, Long livroId);

}
