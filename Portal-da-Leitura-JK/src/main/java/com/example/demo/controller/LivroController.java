package com.example.demo.controller;

import com.example.demo.model.LivroModel;
import com.example.demo.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LivroModel>> buscarPorTitulo(@PathVariable String titulo) {
        List<LivroModel> livros = livroService.buscarPorTitulo(titulo);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros);
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LivroModel>> buscarPorAutor(@PathVariable String autor) {
        List<LivroModel> livros = livroService.buscarPorAutor(autor);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros);
    }

    @GetMapping("/curso/{curso}")
    public ResponseEntity<List<LivroModel>> buscarPorCurso(@PathVariable String curso) {
        List<LivroModel> livros = livroService.buscarPorCurso(curso);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros);
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<LivroModel>> buscarPorGenero(@PathVariable String genero) {
        List<LivroModel> livros = livroService.buscarPorGenero(genero);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros);
    }

    @GetMapping("/editora/{editora}")
    public ResponseEntity<List<LivroModel>> buscarPorEditora(@PathVariable String editora) {
        List<LivroModel> livros = livroService.buscarPorEditora(editora);
        return livros.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<LivroModel>> filtrarLivros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String editora,
            @RequestParam(required = false) String curso) {

        List<LivroModel> livros = livroService.filtrarLivros(titulo, autor, genero, editora, curso);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros);
    }

    @PostMapping
    public ResponseEntity<LivroModel> salvar(@RequestBody LivroModel livro) {
        LivroModel novoLivro = livroService.salvar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }

}
