package com.example.demo.repository;

import com.example.demo.model.AlunoModel;
import com.example.demo.model.LivroModel;
import com.example.demo.model.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaModel, Long> {
    List<ReservaModel> findByAlunoMatricula(String matricula);
    Optional<ReservaModel> findByReservaId(Long reservaId);
    Optional<ReservaModel> findReservaAtivaByAlunoMatricula(String matricula);
    List<ReservaModel> findReservasByAlunoMatricula(String matricula);
    boolean existsByAlunoAndStatusTrue(AlunoModel aluno);
    int countByLivroAndStatusTrue(LivroModel livro);

}
