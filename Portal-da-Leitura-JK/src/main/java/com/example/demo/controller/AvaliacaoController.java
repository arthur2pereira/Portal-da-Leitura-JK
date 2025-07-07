package com.example.demo.controller;

import com.example.demo.dto.AvaliacaoDTO;
import com.example.demo.model.*;
import com.example.demo.service.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<AvaliacaoDTO> criarAvaliacao(@RequestBody AvaliacaoDTO dto) {
        AvaliacaoDTO nova = avaliacaoService.criarAvaliacao(
                dto.getMatricula(),
                dto.getLivroId(),
                dto.getNota(),
                dto.getComentario()
        );

        if (nova == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }

    @PutMapping("/{avaliacaoId}/editar-comentario")
    public ResponseEntity<?> editarComentario(@PathVariable Long avaliacaoId,
                                              @RequestParam String novoComentario) {
        String response = avaliacaoService.editarComentario(avaliacaoId, novoComentario);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                                .body("Erro ao atualizar comentário.");
    }

    @PutMapping("/{avaliacaoId}/alterar-nota")
    public ResponseEntity<?> alterarNota(@PathVariable Long avaliacaoId,
                                         @RequestParam Integer novaNota) {
        String response = avaliacaoService.alterarNota(avaliacaoId, novaNota);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                                .body("Erro ao atualizar nota.");
    }

    @GetMapping("/mais-avaliados")
    public ResponseEntity<List<Map<String, Object>>> buscarLivrosMaisAvaliados() {
        List<Map<String, Object>> livrosComAvaliacoes = avaliacaoService.buscarLivrosMaisAvaliadosComAvaliacoes();
        if (livrosComAvaliacoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(livrosComAvaliacoes);
    }


    @DeleteMapping("/{avaliacaoId}/excluir")
    public ResponseEntity<?> excluir(@PathVariable Long avaliacaoId) {
        boolean isDeleted = avaliacaoService.excluirAvaliacao(avaliacaoId);
        return isDeleted ? ResponseEntity.ok("Avaliação excluída.") : ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                            .body("Erro ao excluir avaliação.");
    }
}
