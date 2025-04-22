package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @GetMapping("/livro/{livroId}")
    public ResponseEntity<List<AvaliacaoModel>> buscarPorLivro(@PathVariable Long livroId) {
        List<AvaliacaoModel> avaliacoes = avaliacaoService.buscarPorLivro(livroId);
        return avaliacoes.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(avaliacoes);
    }

    @PostMapping("/criar")
    public AvaliacaoModel criarAvaliacao(String matricula, Long livroId, Integer nota, String comentario) {
        if (nota == null) {
            throw new RuntimeException("Nota é obrigatória.");
        }

        Optional<AlunoModel> alunoOpt = alunoRepository.findByMatricula(matricula);
        Optional<LivroModel> livroOpt = livroRepository.findById(livroId);

        if (alunoOpt.isEmpty()) throw new RuntimeException("Aluno não encontrado.");
        if (livroOpt.isEmpty()) throw new RuntimeException("Livro não encontrado.");

        AlunoModel aluno = alunoOpt.get();
        LivroModel livro = livroOpt.get();

        boolean emprestou = emprestimoRepository.existsByAlunoAndLivro(aluno, livro);
        if (!emprestou) {
            throw new RuntimeException("Você só pode avaliar livros que já pegou emprestado.");
        }

        AvaliacaoModel avaliacao = new AvaliacaoModel();
        avaliacao.setAluno(aluno);
        avaliacao.setLivro(livro);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);

        return avaliacaoRepository.save(avaliacao);
    }


    @PutMapping("/{avaliacaoId}/editar-comentario")
    public ResponseEntity<?> editarComentario(@PathVariable Long avaliacaoId,
                                              @RequestParam String novoComentario) {
        try {
            avaliacaoService.editarComentario(avaliacaoId, novoComentario);
            return ResponseEntity.ok("Comentário atualizado.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{avaliacaoId}/alterar-nota")
    public ResponseEntity<?> alterarNota(@PathVariable Long avaliacaoId,
                                         @RequestParam Integer novaNota) {
        try {
            avaliacaoService.alterarNota(avaliacaoId, novaNota);
            return ResponseEntity.ok("Nota atualizada.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{avaliacaoId}/excluir")
    public ResponseEntity<?> excluir(@PathVariable Long avaliacaoId) {
        try {
            avaliacaoService.excluirAvaliacao(avaliacaoId);
            return ResponseEntity.ok("Avaliação excluída.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
