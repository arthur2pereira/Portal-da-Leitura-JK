package com.example.demo.repository;

import com.example.demo.model.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaModel, Long> {
    List<ReservaModel> findByAlunoMatricula(String matricula);
    List<ReservaModel> findByLivroLivroId(Long livroId);
    List<ReservaModel> findByStatusAndDataVencimentoBefore(boolean status, LocalDate dataVencimeto);
    Optional<ReservaModel> findByAlunoMatriculaAndLivroLivroId(String matricula, Long livroId);
    Optional<ReservaModel> findReservaAtivaByAlunoMatricula(String matricula);
}
