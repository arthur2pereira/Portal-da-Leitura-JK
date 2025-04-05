package com.example.demo.service;

import com.example.demo.model.EmprestimoModel;
import com.example.demo.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public List<EmprestimoModel> buscarPorAluno(Long matricula) {
        return emprestimoRepository.findByMatricula(matricula);
    }

    public List<EmprestimoModel> buscarPorLivro(Long livroId) {
        return emprestimoRepository.findByLivroId(livroId);
    }

    public List<EmprestimoModel> buscarPorBibliotecario(Long bibliotecarioId) {
        return emprestimoRepository.findByBibliocarioId(bibliotecarioId);
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
}
