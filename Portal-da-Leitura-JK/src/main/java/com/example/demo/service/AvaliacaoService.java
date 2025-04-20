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
        return avaliacaoRepository.findByLivroLivroId(livroId);
    }

    public List<AvaliacaoModel> buscarPorAluno(String matricula) {
        return avaliacaoRepository.findByAluno_Matricula(matricula);
    }

    public AvaliacaoModel salvar(AvaliacaoModel avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    public AvaliacaoModel criarAvaliacao(AvaliacaoModel avaliacao) {
        String matricula = avaliacao.getAluno().getMatricula();
        Long livroId = avaliacao.getLivro().getLivroId();
        boolean jaEmprestou = emprestimoRepository.existsByAlunoMatriculaAndLivro_LivroId(matricula, livroId);
        if (!jaEmprestou) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode avaliar livros que já pegou emprestado.");
        }
        return avaliacaoRepository.save(avaliacao);
    }
}
