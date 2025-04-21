package com.example.demo.controller;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.BibliotecarioDTO;
import com.example.demo.dto.LivroDTO;
import com.example.demo.model.*;
import com.example.demo.service.BibliotecarioService;
import com.example.demo.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bibliotecarios")
public class BibliotecarioController {

    @Autowired
    private BibliotecarioService bibliotecarioService;

    @Autowired
    private LivroService livroService;

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody BibliotecarioModel bibliotecarioLogin) {
        System.out.println("Matricula recebida: " + bibliotecarioLogin.getEmail());  // Debug
        System.out.println("Senha recebida: " + bibliotecarioLogin.getSenha());  // Debug

        Optional<BibliotecarioModel> bibliotecarioOpt = bibliotecarioService.autenticar(
                bibliotecarioLogin.getEmail(),
                bibliotecarioLogin.getSenha()
        );

        if (bibliotecarioOpt.isPresent()) {
            BibliotecarioModel bibliotecario = bibliotecarioOpt.get();
            BibliotecarioDTO dto = new BibliotecarioDTO(
                    bibliotecario.getBibliotecarioId(),
                    bibliotecario.getNome(),
                    bibliotecario.getEmail(),
                    bibliotecario.getSenha()
            );
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Matrícula ou senha inválida");
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

    @PutMapping("/livros/{livroId}")
    public ResponseEntity<LivroModel> editarLivro(@PathVariable Long livroId, @RequestBody LivroDTO livroDTO) {
        Optional<LivroModel> livroOpt = bibliotecarioService.buscarPorlivroId(livroId);

        if (livroOpt.isPresent()) {
            LivroModel livro = livroOpt.get();
            livro.setTitulo(livroDTO.getTitulo());
            livro.setAutor(livroDTO.getAutor());
            livro.setGenero(livroDTO.getGenero());
            livro.setCurso(livroDTO.getCurso());
            livro.setEditora(livroDTO.getEditora());
            livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
            livro.setDescricao(livroDTO.getDescricao());
            livro.setQuantidade(livroDTO.getQuantidade());

            LivroModel livroAtualizado = bibliotecarioService.salvar(livro);
            return ResponseEntity.ok(livroAtualizado);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/livros/{livroId}")
    public ResponseEntity<String> removerLivro(@PathVariable Long livroId) {
        boolean removido = bibliotecarioService.remover(livroId);

        if (removido) {
            return ResponseEntity.ok("Livro removido com sucesso.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado.");
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaModel>> verReservas() {
        List<ReservaModel> reservas = bibliotecarioService.buscarTodasReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/alunos/{matricula}/penalidades")
    public ResponseEntity<List<PenalidadeModel>> historicoPenalidades(@PathVariable String matricula) {
        List<PenalidadeModel> penalidades = bibliotecarioService.buscarPenalidadesPorMatricula(matricula);
        return ResponseEntity.ok(penalidades);
    }

    @GetMapping("/alunos/{matricula}/emprestimos")
    public ResponseEntity<List<EmprestimoModel>> historicoEmprestimos(@PathVariable String matricula) {
        List<EmprestimoModel> emprestimos = bibliotecarioService.buscarEmprestimosPorMatricula(matricula);
        return ResponseEntity.ok(emprestimos);
    }

    @DeleteMapping("/avaliacoes/{avalicacaoId}/moderar")
    public ResponseEntity<String> moderarComentario(@PathVariable Long avaliacaoId) {
        boolean moderado = bibliotecarioService.moderarComentario(avaliacaoId);
        if (moderado) {
            return ResponseEntity.ok("Comentário moderado com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comentário não encontrado.");
    }


    @GetMapping("/{email}")
    public ResponseEntity<BibliotecarioModel> buscarPorEmail(@PathVariable String email) {
        Optional<BibliotecarioModel> bibliotecario = bibliotecarioService.buscarPorEmail(email);
        return bibliotecario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}