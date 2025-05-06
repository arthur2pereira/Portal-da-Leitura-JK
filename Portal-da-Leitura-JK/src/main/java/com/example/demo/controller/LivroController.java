package com.example.demo.controller;

import com.example.demo.dto.LivroDTO;
import com.example.demo.model.AvaliacaoModel;
import com.example.demo.model.LivroModel;
import com.example.demo.service.BibliotecarioService;
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

    @Autowired
    private BibliotecarioService bibliotecarioService;

    @GetMapping("/{livroId}")
    public ResponseEntity<LivroDTO> buscarPorId(@PathVariable Long livroId) {
        LivroModel livro = livroService.buscarPorLivroId(livroId);
        if (livro == null) {
            return ResponseEntity.notFound().build();
            }
        return ResponseEntity.ok(new LivroDTO(livro));
    }

    @GetMapping("/autores")
    public ResponseEntity<List<String>> listarAutores() {
        List<String> autores = livroService.listarAutores();
        return autores.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(autores);
    }

    @GetMapping("/generos")
    public ResponseEntity<List<String>> listarGeneros() {
        List<String> generos = livroService.listarGeneros();
        return generos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(generos);
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<String>> listarCursos() {
        List<String> cursos = livroService.listarCursos();
        return cursos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cursos);
    }

    @GetMapping("/editoras")
    public ResponseEntity<List<String>> listarEditoras() {
        List<String> editoras = livroService.listarEditoras();
        return editoras.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(editoras);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<LivroDTO>> listarLivros() {
        List<LivroModel> livros = livroService.listarLivros();
        if (livros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<LivroDTO> livrosDTO = livros.stream().map(LivroDTO::new).toList();
        return ResponseEntity.ok(livrosDTO);
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LivroDTO>> buscarPorTitulo(@PathVariable String titulo) {
        List<LivroModel> livros = livroService.buscarPorTitulo(titulo);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros.stream().map(LivroDTO::new).toList());
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LivroDTO>> buscarPorAutor(@PathVariable String autor) {
        List<LivroModel> livros = livroService.buscarPorAutor(autor);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros.stream().map(LivroDTO::new).toList());
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<LivroDTO>> buscarPorGenero(@PathVariable String genero) {
        List<LivroModel> livros = livroService.buscarPorGenero(genero);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros.stream().map(LivroDTO::new).toList());
    }

    @GetMapping("/curso/{curso}")
    public ResponseEntity<List<LivroDTO>> buscarPorCurso(@PathVariable String curso) {
        List<LivroModel> livros = livroService.buscarPorCurso(curso);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros.stream().map(LivroDTO::new).toList());
    }

    @GetMapping("/editora/{editora}")
    public ResponseEntity<List<LivroDTO>> buscarPorEditora(@PathVariable String editora) {
        List<LivroModel> livros = livroService.buscarPorEditora(editora);
        return livros.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros.stream().map(LivroDTO::new).toList());
    }


    @PostMapping("/livros")
    public ResponseEntity<LivroDTO> salvarLivro(@RequestBody LivroDTO livroDTO) {
        LivroModel livroModel = livroService.salvarLivro(livroDTO);
        return new ResponseEntity<>(new LivroDTO(livroModel), HttpStatus.CREATED);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LivroDTO>> buscarLivros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String editora,
            @RequestParam(required = false) String curso) {

        List<LivroModel> livros = livroService.filtrarLivros(titulo, autor, genero, editora, curso);
        List<LivroDTO> livrosDTO = livros.stream().map(livro -> new LivroDTO(
                livro.getLivroId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getGenero(),
                livro.getCurso(),
                livro.getEditora(),
                livro.getAnoPublicacao(),
                livro.getDescricao(),
                livro.getQuantidade()
        )).toList();

        return ResponseEntity.ok(livrosDTO);
    }

    @GetMapping("/disponivel/{livroId}")
    public ResponseEntity<Boolean> verificarDisponibilidade(@PathVariable Long livroId) {
        boolean disponivel = livroService.estaDisponivel(livroId);
        return ResponseEntity.ok(disponivel);
    }

    @PatchMapping("/estoque/reduzir/{livroId}")
    public ResponseEntity<LivroDTO> reduzirEstoque(@PathVariable Long livroId) {
        LivroModel livroAtualizado = livroService.reduzirEstoque(livroId);
        if (livroAtualizado != null) {
            LivroDTO livroDTO = new LivroDTO(
                    livroAtualizado.getLivroId(),
                    livroAtualizado.getTitulo(),
                    livroAtualizado.getAutor(),
                    livroAtualizado.getGenero(),
                    livroAtualizado.getCurso(),
                    livroAtualizado.getEditora(),
                    livroAtualizado.getAnoPublicacao(),
                    livroAtualizado.getDescricao(),
                    livroAtualizado.getQuantidade()
            );
            return ResponseEntity.ok(livroDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/estoque/aumentar/{livroId}/{quantidade}")
    public ResponseEntity<LivroDTO> aumentarEstoque(@PathVariable Long livroId, @PathVariable Integer quantidade) {
        LivroModel livroAtualizado = livroService.aumentarEstoque(livroId, quantidade);
        if (livroAtualizado != null) {
            LivroDTO livroDTO = new LivroDTO(
                    livroAtualizado.getLivroId(),
                    livroAtualizado.getTitulo(),
                    livroAtualizado.getAutor(),
                    livroAtualizado.getGenero(),
                    livroAtualizado.getCurso(),
                    livroAtualizado.getEditora(),
                    livroAtualizado.getAnoPublicacao(),
                    livroAtualizado.getDescricao(),
                    livroAtualizado.getQuantidade()
            );
            return ResponseEntity.ok(livroDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/avaliacao/media/{livroId}")
    public ResponseEntity<Double> obterMediaAvaliacao(@PathVariable Long livroId) {
        double media = livroService.getMediaAvaliacao(livroId);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/avaliacoes/{livroId}")
    public ResponseEntity<List<AvaliacaoModel>> listarAvaliacoes(@PathVariable Long livroId) {
        List<AvaliacaoModel> avaliacoes = livroService.listarAvaliacoes(livroId);
        return ResponseEntity.ok(avaliacoes);
    }
}
