package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<EmprestimoModel, Long> {

    Optional<EmprestimoModel> findByEmprestimoId(Long emprestimoId);
    List<EmprestimoModel> findByAlunoMatricula(String matricula);
    List<EmprestimoModel> findByBibliotecarioBibliotecarioId(Long bibliotecarioId);
    boolean existsByAlunoAndLivro(AlunoModel aluno, LivroModel livro);
    int countByLivroAndDataDevolucaoIsNull(LivroModel livro);
}
