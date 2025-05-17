package com.example.demo.service;

import com.example.demo.dto.ReservaDTO;
import com.example.demo.model.AlunoModel;
import com.example.demo.model.LivroModel;
import com.example.demo.model.ReservaModel;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.LivroRepository;
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

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public List<ReservaModel> buscarPorAluno(String matricula) {
        return reservaRepository.findByAlunoMatricula(matricula);
    }

    public boolean estaAtiva(Long reservaId) {
        ReservaModel reserva = reservaRepository.findByReservaId(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada."));
        return reserva.getDataVencimento().isAfter(LocalDate.now());
    }

    public ReservaModel criarReserva(String matricula, Long livroId) {
        Optional<AlunoModel> alunoOpt = alunoRepository.findByMatricula(matricula);
        if (alunoOpt.isEmpty()) throw new RuntimeException("Aluno não encontrado.");

        Optional<LivroModel> livroOpt = livroRepository.findByLivroId(livroId);
        if (livroOpt.isEmpty()) throw new RuntimeException("Livro não encontrado.");

        LivroModel livro = livroOpt.get();
        if (livro.getQuantidade() <= 0) throw new RuntimeException("Livro indisponível.");

        ReservaModel nova = new ReservaModel();
        nova.setAluno(alunoOpt.get());
        nova.setLivro(livro);
        nova.setDataReserva(LocalDate.now());
        nova.setDataVencimento(LocalDate.now().plusDays(3));

        return reservaRepository.save(nova);
    }


    public void cancelarReserva(Long reservaId) {
        ReservaModel reserva = reservaRepository.findByReservaId(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada."));
        reservaRepository.delete(reserva);
    }

    public String verificarValidade(Long reservaId) {
        ReservaModel reserva = reservaRepository.findByReservaId(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada."));
        LocalDate hoje = LocalDate.now();
        if (hoje.isAfter(reserva.getDataVencimento())) {
            return "Reserva vencida.";
        } else {
            long diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(hoje, reserva.getDataVencimento());
            return "Reserva válida por mais " + diasRestantes + " dias.";
        }
    }

    public ReservaDTO converterParaDTO(ReservaModel model) {
        return new ReservaDTO(
                model.getReservaId(),
                model.getAluno().getMatricula(),
                model.getLivro().getLivroId(),
                model.getLivro().getTitulo(),
                model.getStatus(),
                model.getDataReserva(),
                model.getDataVencimento()
        );
    }
}
