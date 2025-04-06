package com.example.demo.service;

import com.example.demo.model.AlunoModel;
import com.example.demo.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Optional<AlunoModel> buscarPorMatricula(Long matricula) {
        return alunoRepository.findByMatricula(matricula);
    }

    public List<AlunoModel> buscarPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public AlunoModel salvar(AlunoModel aluno) {
        return alunoRepository.save(aluno);
    }

    public boolean alunoExiste(Long matricula) {
        return alunoRepository.existsByMatricula(matricula);
    }
}
