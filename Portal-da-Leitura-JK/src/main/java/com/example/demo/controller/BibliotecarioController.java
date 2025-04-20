package com.example.demo.controller;

import com.example.demo.dto.LivroDTO;
import com.example.demo.model.BibliotecarioModel;
import com.example.demo.model.LivroModel;
import com.example.demo.service.BibliotecarioService;
import com.example.demo.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bibliotecarios")
public class BibliotecarioController {

    @Autowired
    private BibliotecarioService bibliotecarioService;

    @Autowired
    private LivroService livroService;

    // Endpoint para cadastrar um livro
    @PostMapping("/livros")
    public ResponseEntity<LivroModel> salvarLivro(@RequestBody LivroDTO livroDTO) {
        LivroModel livroModel = new LivroModel();

        // Convertendo o DTO para o modelo de livro
        livroModel.setTitulo(livroDTO.getTitulo());
        livroModel.setAutor(livroDTO.getAutor());
        livroModel.setGenero(livroDTO.getGenero());
        livroModel.setCurso(livroDTO.getCurso());
        livroModel.setEditora(livroDTO.getEditora());
        livroModel.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livroModel.setDescricao(livroDTO.getDescricao());
        livroModel.setQuantidade(livroDTO.getQuantidade());

        LivroModel livroSalvo = livroService.salvar(livroModel);

        return new ResponseEntity<>(livroSalvo, HttpStatus.CREATED);
    }

    @GetMapping("/{email}")
    public ResponseEntity<BibliotecarioModel> buscarPorEmail(@PathVariable String email) {
        Optional<BibliotecarioModel> bibliotecario = bibliotecarioService.buscarPorEmail(email);
        return bibliotecario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<BibliotecarioModel> salvar(@RequestBody BibliotecarioModel bibliotecario) {
        BibliotecarioModel novoBibliotecario = bibliotecarioService.salvar(bibliotecario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoBibliotecario);
    }
}
