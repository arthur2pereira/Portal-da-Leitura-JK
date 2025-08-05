package com.example.demo.service;

import com.example.demo.dto.ReservaDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private BibliotecarioRepository bibliotecarioRepository;

    public List<ReservaModel> buscarPorAluno(String matricula) {
        return reservaRepository.findByAlunoMatricula(matricula);
    }

    public boolean estaAtiva(Long reservaId) {
        ReservaModel reserva = reservaRepository.findByReservaId(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada."));
        return reserva.getDataVencimento().isAfter(LocalDate.now());
    }

    public ReservaModel criarReserva(String matricula, Long livroId) {
        AlunoModel aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        LivroModel livro = livroRepository.findByLivroId(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        boolean temReservaAtiva = reservaRepository.existsByAlunoAndStatusTrue(aluno);
        if (temReservaAtiva) {
            throw new RuntimeException("Você já possui uma reserva ativa.");
        }

        int reservasAtivas = reservaRepository.countByLivroAndStatusTrue(livro);
        int disponiveis = livro.getQuantidade() - reservasAtivas;

        if (disponiveis <= 0) {
            throw new RuntimeException("Todas as unidades deste livro já estão reservadas.");
        }

        ReservaModel reserva = new ReservaModel();
        reserva.setAluno(aluno);
        reserva.setLivro(livro);
        reserva.setStatus(true);
        reserva.setDataReserva(LocalDate.now());
        reserva.setDataVencimento(LocalDate.now().plusDays(3));

        return reservaRepository.save(reserva);
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

    public void transformarReservaEmEmprestimo(Long reservaId) {
        ReservaModel reserva = reservaRepository.findByReservaId(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada."));

        LivroModel livro = reserva.getLivro();

        if (livro.getQuantidade() < 1) {
            throw new RuntimeException("Livro indisponível para empréstimo.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        BibliotecarioModel bibliotecario = bibliotecarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Bibliotecário não encontrado."));


        EmprestimoModel emprestimo = new EmprestimoModel();
        emprestimo.setAluno(reserva.getAluno());
        emprestimo.setLivro(livro);
        emprestimo.setBibliotecario(bibliotecario);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataVencimento(LocalDate.now().plusDays(7));
        emprestimo.setStatus("Ativa");

        emprestimoRepository.save(emprestimo);

        livro.setQuantidade(livro.getQuantidade() - 1);
        livroRepository.save(livro);

        reservaRepository.delete(reserva);
    }

    public ReservaDTO converterParaDTO(ReservaModel model) {
        return new ReservaDTO(
                model.getReservaId(),
                model.getAluno().getMatricula(),
                model.getAluno().getNome(),
                model.getLivro().getLivroId(),
                model.getLivro().getTitulo(),
                model.getStatus(),
                model.getDataReserva(),
                model.getDataVencimento()
        );
    }
}
