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

    @GetMapping("/tudo")
    public List<LivroModel> listarLivros() {
        return livroService.listarLivros();
    }


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

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<LivroModel>> buscarPorGenero(@PathVariable String genero) {
        List<LivroModel> livros = livroService.buscarPorGenero(genero);
        return livros.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(livros);
    }

    @GetMapping("/curso/{curso}")
    public ResponseEntity<List<LivroModel>> buscarPorCurso(@PathVariable String curso) {
        List<LivroModel> livros = livroService.buscarPorCurso(curso);
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


    @PostMapping("/livros")
    public ResponseEntity<LivroModel> salvarLivro(@RequestBody LivroDTO livroDTO) {
        LivroModel livroModel = new LivroModel();
        livroModel.setTitulo(livroDTO.getTitulo());
        livroModel.setAutor(livroDTO.getAutor());
        livroModel.setGenero(livroDTO.getGenero());
        livroModel.setCurso(livroDTO.getCurso());
        livroModel.setEditora(livroDTO.getEditora());
        livroModel.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livroModel.setDescricao(livroDTO.getDescricao());
        livroModel.setQuantidade(livroDTO.getQuantidade());

        LivroModel livroSalvo = bibliotecarioService.salvar(livroModel);

        return new ResponseEntity<>(livroSalvo, HttpStatus.CREATED);
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

    // Endpoint para obter média das avaliações de um livro
    @GetMapping("/avaliacao/media/{livroId}")
    public ResponseEntity<Double> obterMediaAvaliacao(@PathVariable Long livroId) {
        double media = livroService.getMediaAvaliacao(livroId);
        return ResponseEntity.ok(media);
    }


    // Endpoint para listar as avaliações de um livro
    @GetMapping("/avaliacoes/{livroId}")
    public ResponseEntity<List<AvaliacaoModel>> listarAvaliacoes(@PathVariable Long livroId) {
        List<AvaliacaoModel> avaliacoes = livroService.listarAvaliacoes(livroId);
        return ResponseEntity.ok(avaliacoes);
    }
}
