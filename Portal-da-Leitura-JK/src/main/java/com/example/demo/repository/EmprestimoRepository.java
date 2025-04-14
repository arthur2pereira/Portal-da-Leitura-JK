package com.example.demo.repository;

import com.example.demo.model.EmprestimoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<EmprestimoModel, Long> {

    List<EmprestimoModel> findByAluno_Matricula(String matricula);
    List<EmprestimoModel> findByLivro_LivroId(Long livroId);
    List<EmprestimoModel> findByDataVencimentoBeforeAndDataDevolucaoIsNull(LocalDate data);
    List<EmprestimoModel> findByBibliotecario_BibliotecarioId(Long bibliotecarioId);
    boolean existsByAlunoMatriculaAndLivro_LivroId(String matricula, Long livroId);

}
