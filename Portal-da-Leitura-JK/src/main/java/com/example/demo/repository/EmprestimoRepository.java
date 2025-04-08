package com.example.demo.repository;

import com.example.demo.model.EmprestimoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<EmprestimoModel, Long> {
    // Consultar empréstimos por aluno
    List<EmprestimoModel> findByAlunoMatricula(String matricula);

    // Consultar empréstimos por livro
    List<EmprestimoModel> findByLivroId(Long livroId);

    // Consultar empréstimos atrasados
    List<EmprestimoModel> findByDataVencimentoBeforeAndDataDevolucaoIsNull(LocalDate data);

    // Qual Bibliotecario aceitou tal emprestimo
    List<EmprestimoModel> findByBibliotecarioId(Long bibliotecarioId);

    // Se o aluno já pegou o livro tal livro ou não
    boolean existsByAlunoMatriculaAndLivroId(String matricula, Long livroId);

}
