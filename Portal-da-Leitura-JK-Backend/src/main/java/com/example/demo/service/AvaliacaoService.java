package com.example.demo.service;

import com.example.demo.model.AvaliacaoModel;
import com.example.demo.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public List<AvaliacaoModel> buscarPorLivro(Long livroId) {
        return avaliacaoRepository.findByLivroId(livroId);
    }

    public List<AvaliacaoModel> buscarPorAluno(Long alunoId) {
        return avaliacaoRepository.findByAlunoId(alunoId);
    }

    public AvaliacaoModel salvar(AvaliacaoModel avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }
}
