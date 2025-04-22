package com.example.demo.service;

import com.example.demo.dto.EmprestimoDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private BibliotecarioRepository bibliotecarioRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private LivroRepository livroRepository;

    public List<EmprestimoModel> buscarPorAluno(String matricula) {
        return emprestimoRepository.findByAlunoMatricula(matricula);
    }

    public List<EmprestimoModel> buscarPorBibliotecario(Long bibliotecarioId) {
        return emprestimoRepository.findByBibliotecarioBibliotecarioId(bibliotecarioId);
    }

    public boolean estaVencido(Long emprestimoId) {
        EmprestimoModel e = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado."));

        return e.getDataDevolucao() == null && e.getDataVencimento().isBefore(LocalDate.now());
    }

    public EmprestimoModel registrarEmprestimo(String matricula, Long livroId, String emailBibliotecario) {
        AlunoModel aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        LivroModel livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        if (livro.getQuantidade() <= 0)
            throw new RuntimeException("Livro indisponível.");

        BibliotecarioModel bibliotecario = bibliotecarioRepository.findByEmail(emailBibliotecario)
                .orElseThrow(() -> new RuntimeException("Bibliotecário não encontrado."));

        EmprestimoModel emprestimo = new EmprestimoModel();
        emprestimo.setAluno(aluno);
        emprestimo.setLivro(livro);
        emprestimo.setBibliotecario(bibliotecario);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataVencimento(LocalDate.now().plusDays(7));

        return emprestimoRepository.save(emprestimo);
    }

    public void registrarDevolucao(Long emprestimoId) {
        EmprestimoModel emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado."));
        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimoRepository.save(emprestimo);
    }

    public boolean renovarEmprestimoPorAluno(Long emprestimoId, String matriculaAluno) {
        EmprestimoModel emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        if (!emprestimo.getAluno().getMatricula().equals(matriculaAluno)) {
            throw new RuntimeException("Você não tem permissão pra renovar esse empréstimo");
        }
        if (emprestimo.getDataDevolucao() != null) {
            throw new RuntimeException("Esse empréstimo já foi finalizado");
        }
        if (emprestimo.getRenovacoes() >= 1) {
            throw new RuntimeException("Você só pode renovar uma vez");
        }
        emprestimo.setDataVencimento(emprestimo.getDataVencimento().plusDays(7));
        emprestimo.setRenovacoes(emprestimo.getRenovacoes() + 1);

        emprestimoRepository.save(emprestimo);
        return true;
    }

    public void renovarEmprestimoPorBibliotecario(Long emprestimoId, int dias, String emailBibliotecario) {
        EmprestimoModel emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        if (emprestimo.getDataDevolucao() != null) {
            throw new RuntimeException("Esse empréstimo já foi finalizado");
        }

        boolean autorizado = bibliotecarioRepository.existsByEmail(emailBibliotecario);
        if (!autorizado) {
            throw new RuntimeException("Bibliotecário não autorizado");
        }

        emprestimo.setDataVencimento(emprestimo.getDataVencimento().plusDays(dias));
        emprestimoRepository.save(emprestimo);
    }

    public int diasDeAtraso(Long emprestimoId) {
        EmprestimoModel e = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado."));

        if (e.getDataDevolucao() == null) return 0;

        long dias = ChronoUnit.DAYS.between(e.getDataVencimento(), e.getDataDevolucao());
        return dias > 0 ? (int) dias : 0;
    }

    public EmprestimoDTO converterParaDTO(EmprestimoModel model) {
        return new EmprestimoDTO(
                model.getEmprestimoId(),
                model.getAluno().getMatricula(),
                model.getLivro().getLivroId(),
                model.getBibliotecario().getEmail(),
                model.getDataEmprestimo(),
                model.getDataVencimento(),
                model.getDataDevolucao(),
                model.getRenovacoes(),
                model.getStatus()
        );
    }
}

