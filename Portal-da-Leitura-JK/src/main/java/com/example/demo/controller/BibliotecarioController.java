package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AlunoService;
import com.example.demo.service.BibliotecarioService;
import com.example.demo.service.LivroService;
import com.example.demo.service.ReservaService;
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

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody BibliotecarioDTO bibliotecarioLogin) {

        Optional<TokenDTO> tokenOpt = bibliotecarioService.autenticar(
                bibliotecarioLogin.getEmail(),
                bibliotecarioLogin.getSenha()
        );

        System.out.println("Login recebido: " + bibliotecarioLogin.getEmail());  // Debug
        System.out.println("Senha recebida: " + bibliotecarioLogin.getSenha());  // Debug

        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok(tokenOpt.get());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválida");
    }

    @GetMapping("/listaDeAlunos")
    public ResponseEntity<List<AlunoDTO>> listarAlunos() {
        List<AlunoDTO> alunos = alunoService.listarTodos();
        return ResponseEntity.ok(alunos);
    }

    @PostMapping("/reservas/{reservaId}/confirmar-retirada")
    public ResponseEntity<?> confirmarRetirada(@PathVariable Long reservaId) {
        try {
            reservaService.transformarReservaEmEmprestimo(reservaId);
            return ResponseEntity.ok("Retirada confirmada e empréstimo registrado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarAlunoComoAdmin(@RequestBody AlunoDTO dto) {
        try {
            AlunoDTO atualizado = alunoService.atualizarAluno(dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    @DeleteMapping("/alunos/{matricula}")
    public ResponseEntity<String> deletarAluno(@PathVariable String matricula) {
        alunoService.deletarAlunoPorMatricula(matricula);
        return ResponseEntity.ok("Aluno deletado com sucesso.");
    }

    @DeleteMapping("/reservas/cancelar/{reservaId}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long reservaId) {
        try {
            reservaService.cancelarReserva(reservaId);
            return ResponseEntity.ok("Reserva cancelada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/livros")
    public ResponseEntity<LivroDTO> salvarLivro(@RequestBody LivroDTO livroDTO) {
        LivroDTO livroSalvo = bibliotecarioService.salvarLivro(livroDTO);
        return new ResponseEntity<>(livroSalvo, HttpStatus.CREATED);
    }

    @PutMapping("/livros/{livroId}")
    public ResponseEntity<LivroDTO> editarLivro(@PathVariable Long livroId, @RequestBody LivroDTO livroDTO) {
        Optional<LivroDTO> livroOpt = bibliotecarioService.buscarLivroPorId(livroId);

        if (livroOpt.isPresent()) {
            LivroDTO livroAtualizado = bibliotecarioService.editarLivro(livroId, livroDTO);
            return ResponseEntity.ok(livroAtualizado);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/livros/{livroId}")
    public ResponseEntity<String> removerLivro(@PathVariable Long livroId) {
        boolean removido = bibliotecarioService.removerLivro(livroId);

        if (removido) {
            return ResponseEntity.ok("Livro removido com sucesso.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado.");
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaDTO>> verReservas() {
        List<ReservaDTO> reservas = bibliotecarioService.listarTodasReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/alunos/{matricula}/penalidades")
    public ResponseEntity<List<PenalidadeDTO>> historicoPenalidades(@PathVariable String matricula) {
        List<PenalidadeDTO> penalidades = bibliotecarioService.listarPenalidadesDoAluno(matricula);
        return ResponseEntity.ok(penalidades);
    }

    @GetMapping("/alunos/{matricula}/emprestimos")
    public ResponseEntity<List<EmprestimoDTO>> historicoEmprestimos(@PathVariable String matricula) {
        List<EmprestimoDTO> emprestimos = bibliotecarioService.listarEmprestimosDoAluno(matricula);
        return ResponseEntity.ok(emprestimos);
    }

    @DeleteMapping("/avaliacoes/{avalicacaoId}/moderar")
    public ResponseEntity<String> moderarComentario(@PathVariable Long avaliacaoId) {
        boolean moderado = bibliotecarioService.removerComentario(avaliacaoId);
        if (moderado) {
            return ResponseEntity.ok("Comentário moderado com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comentário não encontrado.");
    }

    @GetMapping("/{email}")
    public ResponseEntity<BibliotecarioDTO> buscarPorEmail(@PathVariable String email) {
        Optional<BibliotecarioDTO> bibliotecario = bibliotecarioService.buscarPorEmail(email);
        return bibliotecario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}