package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public AvaliacaoModel criarAvaliacao(String matricula, Long livroId, Integer nota, String comentario) {
        if (nota == null) {
            throw new RuntimeException("Nota é obrigatória.");
        }

        Optional<AlunoModel> alunoOpt = alunoRepository.findByMatricula(matricula);
        Optional<LivroModel> livroOpt = livroRepository.findById(livroId);

        if (alunoOpt.isEmpty()) throw new RuntimeException("Aluno não encontrado.");
        if (livroOpt.isEmpty()) throw new RuntimeException("Livro não encontrado.");

        AvaliacaoModel avaliacao = new AvaliacaoModel();
        avaliacao.setAluno(alunoOpt.get());
        avaliacao.setLivro(livroOpt.get());
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);

        return avaliacaoRepository.save(avaliacao);
    }

    public void editarComentario(Long avaliacaoId, String novoComentario) {
        AvaliacaoModel avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));
        avaliacao.setComentario(novoComentario);
        avaliacaoRepository.save(avaliacao);
    }

    public void alterarNota(Long avaliacaoId, Integer novaNota) {
        if (novaNota == null) throw new RuntimeException("Nota não pode ser nula.");
        AvaliacaoModel avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));
        avaliacao.setNota(novaNota);
        avaliacaoRepository.save(avaliacao);
    }

    public void excluirAvaliacao(Long avaliacaoId) {
        AvaliacaoModel avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));
        avaliacaoRepository.delete(avaliacao);
    }

    public List<AvaliacaoModel> buscarPorLivro(Long livroId) {
        return avaliacaoRepository.findByLivroLivroId(livroId);
    }

    public List<AvaliacaoModel> buscarPorAluno(String matricula) {
        return avaliacaoRepository.findByAlunoMatricula(matricula);
    }
}
