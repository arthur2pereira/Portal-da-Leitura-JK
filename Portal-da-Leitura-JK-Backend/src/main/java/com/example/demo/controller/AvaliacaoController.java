package com.example.demo.controller;

import com.example.demo.model.AvaliacaoModel;
import com.example.demo.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping("/livro/{livroId}")
    public ResponseEntity<List<AvaliacaoModel>> buscarPorLivro(@PathVariable Long livroId) {
        List<AvaliacaoModel> avaliacoes = avaliacaoService.buscarPorLivro(livroId);
        return avaliacoes.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(avaliacoes);
    }

    @PostMapping
    public ResponseEntity<AvaliacaoModel> salvar(@RequestBody AvaliacaoModel avaliacao) {
        AvaliacaoModel novaAvaliacao = avaliacaoService.salvar(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }
}
