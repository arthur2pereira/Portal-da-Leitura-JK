package com.example.demo.service;

import com.example.demo.dto.AvaliacaoDTO;
import com.example.demo.dto.LivroDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class    AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public AvaliacaoDTO criarAvaliacao(String matricula, Long livroId, Integer nota, String comentario) {
        if (nota == null) {
            System.out.println("Erro: Nota é obrigatória.");
            return null;
        }

        Optional<AlunoModel> alunoOpt = alunoRepository.findByMatricula(matricula);
        if (alunoOpt.isEmpty()) {
            System.out.println("Erro: Aluno não encontrado.");
            return null;
        }
        AlunoModel aluno = alunoOpt.get();

        Optional<LivroModel> livroOpt = livroRepository.findByLivroId(livroId);
        if (livroOpt.isEmpty()) {
            System.out.println("Erro: Livro não encontrado.");
            return null;
        }
        LivroModel livro = livroOpt.get();

        boolean emprestou = emprestimoRepository.existsByAlunoAndLivro(aluno, livro);
        if (!emprestou) {
            System.out.println("Erro: Você só pode avaliar livros que já pegou emprestado.");
            return null;
        }

        AvaliacaoModel avaliacao = new AvaliacaoModel();
        avaliacao.setAluno(alunoOpt.get());
        avaliacao.setLivro(livroOpt.get());
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);

        AvaliacaoModel salvo = avaliacaoRepository.save(avaliacao);
        return converterParaDTO(salvo);
    }

    public String editarComentario(Long avaliacaoId, String novoComentario) {
        AvaliacaoModel avaliacao = avaliacaoRepository.findByAvaliacaoId(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));

        avaliacao.setComentario(novoComentario);
        avaliacaoRepository.save(avaliacao);
        return "Comentário atualizado.";
    }

    public String alterarNota(Long avaliacaoId, Integer novaNota) {
        if (novaNota == null) {
            throw new IllegalArgumentException("Nota não pode ser nula.");
        }

        AvaliacaoModel avaliacao = avaliacaoRepository.findByAvaliacaoId(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));

        avaliacao.setNota(novaNota);
        avaliacaoRepository.save(avaliacao);
        return "Nota atualizada.";
    }

    public boolean excluirAvaliacao(Long avaliacaoId) {
        AvaliacaoModel avaliacao = avaliacaoRepository.findByAvaliacaoId(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));
        avaliacaoRepository.delete(avaliacao);
        return true;
    }

    public List<AvaliacaoModel> buscarPorLivro(Long livroId) {
        return avaliacaoRepository.findByLivroLivroId(livroId);
    }

    public List<LivroModel> buscarLivrosMaisAvaliados() {
        List<Object[]> resultados = avaliacaoRepository.buscarLivrosMaisAvaliados();
        List<LivroModel> livrosMaisAvaliados = new ArrayList<>();
        for (Object[] resultado : resultados) {
            LivroModel livro = (LivroModel) resultado[0];
            livrosMaisAvaliados.add(livro);
        }
        return livrosMaisAvaliados;
    }

    private AvaliacaoDTO converterParaDTO(AvaliacaoModel model) {
        return new AvaliacaoDTO(
                model.getAvaliacaoId(),
                model.getAluno().getMatricula(),
                model.getLivro().getLivroId(),
                model.getNota(),
                model.getComentario()
        );
    }

    public static LivroDTO converterParaDTO(LivroModel livro) {
        return new LivroDTO(
                livro.getLivroId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getGenero(),
                livro.getCurso(),
                livro.getEditora(),
                livro.getAnoPublicacao(),
                livro.getDescricao(),
                livro.getQuantidade()
        );
    }
}
