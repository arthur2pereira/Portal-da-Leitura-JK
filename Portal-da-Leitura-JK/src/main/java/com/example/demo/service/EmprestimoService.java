package com.example.demo.service;

import com.example.demo.model.EmprestimoModel;
import com.example.demo.repository.BibliotecarioRepository;
import com.example.demo.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private BibliotecarioRepository bibliotecarioRepository;

    public List<EmprestimoModel> buscarPorAluno(String matricula) {
        return emprestimoRepository.findByAluno_Matricula(matricula);
    }

    public List<EmprestimoModel> buscarPorLivro(Long livroId) {
        return emprestimoRepository.findByLivro_LivroId(livroId);
    }

    public List<EmprestimoModel> buscarPorBibliotecario(Long bibliotecarioId) {
        return emprestimoRepository.findByBibliotecario_BibliotecarioId(bibliotecarioId);
    }

    public List<EmprestimoModel> buscarEmprestimosAtrasados(LocalDate data) {
        return emprestimoRepository.findByDataVencimentoBeforeAndDataDevolucaoIsNull(data);
    }

    public EmprestimoModel salvar(EmprestimoModel emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }

    public void devolverEmprestimo(Long emprestimoId) {
        emprestimoRepository.deleteById(emprestimoId);
    }

    public void renovarEmprestimoPorAluno(Long emprestimoId, String matriculaAluno) {
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
}

