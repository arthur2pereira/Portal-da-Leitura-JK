package com.example.demo.service;

import com.example.demo.model.AvaliacaoModel;
import com.example.demo.repository.AvaliacaoRepository;
import com.example.demo.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    private EmprestimoRepository emprestimoRepository;

    public List<AvaliacaoModel> buscarPorLivro(Long livroId) {
        return avaliacaoRepository.findByLivroId(livroId);
    }

    public List<AvaliacaoModel> buscarPorAluno(Long matricula) {
        return avaliacaoRepository.findByMatricula(matricula);
    }

    public AvaliacaoModel salvar(AvaliacaoModel avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    public AvaliacaoModel criarAvaliacao(AvaliacaoModel avaliacao) {
        Long matricula = avaliacao.getAluno().getMatricula();
        Long livroId = avaliacao.getLivro().getId();
        boolean jaEmprestou = emprestimoRepository.existsByAlunoMatriculaAndLivroId(matricula, livroId);
        if (!jaEmprestou) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode avaliar livros que já pegou emprestado.");
        }
        return avaliacaoRepository.save(avaliacao);
    }
}
