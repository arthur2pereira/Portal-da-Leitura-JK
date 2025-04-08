package com.example.demo.service;

import com.example.demo.model.ReservaModel;
import com.example.demo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public List<ReservaModel> buscarPorAluno(String matricula) {
        return reservaRepository.findByAlunoMatricula(matricula);
    }

    public List<ReservaModel> buscarPorLivro(Long livroId) {
        return reservaRepository.findByLivroId(livroId);
    }

    public List<ReservaModel> buscarReservasNaoRetiradas(String status, LocalDate data) {
        return reservaRepository.findByStatusAndDataVencimentoBefore(status, data);
    }

    public Optional<ReservaModel> buscarPorAlunoELivro(String matricula, Long livroId) {
        return reservaRepository.findByAlunoMatriculaAndLivroId(matricula, livroId);
    }

    public ReservaModel salvar(ReservaModel reserva) {
        return reservaRepository.save(reserva);
    }

    public void cancelarReserva(Long reservaId) {
        reservaRepository.deleteById(reservaId);
    }
}
