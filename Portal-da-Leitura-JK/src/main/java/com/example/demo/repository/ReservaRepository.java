package com.example.demo.repository;

import com.example.demo.model.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaModel, Long> {
    // Consultar reservas por aluno
    List<ReservaModel> findByAlunoMatricula(Long matricula);

    // Consultar reservas por livro
    List<ReservaModel> findByLivroId(Long livroId);

    // Consultar reservas não retiradas (vencidas)
    List<ReservaModel> findByStatusAndDataVencimentoBefore(String status, LocalDate data);

    // Consultar uma reserva específica por aluno e livro
    Optional<ReservaModel> findByAlunoMatriculaAndLivroId(Long matricula, Long livroId);
}
