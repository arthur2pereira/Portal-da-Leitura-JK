package com.example.demo.controller;

import com.example.demo.dto.LivroDTO;
import com.example.demo.model.AvaliacaoModel;
import com.example.demo.model.LivroModel;
import com.example.demo.service.BibliotecarioService;
import com.example.demo.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // Listar todos livro paginados
    @GetMapping("/listar")
    public ResponseEntity<?> listarLivros(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho) {
        Page<LivroModel> livrosPage = livroService.listarLivrosPaginados(pagina, tamanho);

        if (livrosPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<LivroDTO> livrosDTO = livrosPage.map(LivroDTO::new).toList();

        Map<String, Object> resposta = Map.of(
                "livros", livrosDTO,
                "paginaAtual", livrosPage.getNumber(),
                "totalPaginas", livrosPage.getTotalPages(),
                "totalElementos", livrosPage.getTotalElements()
        );

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> buscarLivros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String editora,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho
    ) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<LivroModel> livrosPage = livroService.buscarComFiltros(titulo, autor, genero, curso, editora, pageable);

        if (livrosPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<LivroDTO> livrosDTO = livrosPage.map(LivroDTO::new).toList();

        Map<String, Object> resposta = Map.of(
                "livros", livrosDTO,
                "paginaAtual", livrosPage.getNumber(),
                "totalPaginas", livrosPage.getTotalPages(),
                "totalElementos", livrosPage.getTotalElements()
        );

        return ResponseEntity.ok(resposta);
    }

    // Buscar por título paginado
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorTitulo(
            @RequestParam(required = false) String titulo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho) {
        Page<LivroModel> livrosPage;
        if (titulo != null && !titulo.isEmpty()) {
            livrosPage = livroService.buscarPorTituloPaginado(titulo, pagina, tamanho);
        } else {
            livrosPage = livroService.listarLivrosPaginados(pagina, tamanho);
        }

        if (livrosPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<LivroDTO> livrosDTO = livrosPage.map(LivroDTO::new).toList();

        Map<String, Object> resposta = Map.of(
                "livros", livrosDTO,
                "paginaAtual", livrosPage.getNumber(),
                "totalPaginas", livrosPage.getTotalPages(),
                "totalElementos", livrosPage.getTotalElements()
        );

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/livros")
    public ResponseEntity<LivroDTO> salvarLivro(@RequestBody LivroDTO livroDTO) {
        LivroModel livroModel = livroService.salvarLivro(livroDTO);
        return new ResponseEntity<>(new LivroDTO(livroModel), HttpStatus.CREATED);
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

    @GetMapping("/quantidade-disponivel/{livroId}")
    public ResponseEntity<Integer> obterQuantidadeDisponivel(@PathVariable Long livroId) {
        int disponivel = livroService.getQuantidadeDisponivel(livroId);
        return ResponseEntity.ok(disponivel);
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
