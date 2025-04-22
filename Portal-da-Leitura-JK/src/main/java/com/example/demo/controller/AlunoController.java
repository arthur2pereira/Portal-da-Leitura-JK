package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.AlunoModel;
import com.example.demo.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping("/{matricula}")
    public ResponseEntity<AlunoDTO> buscarPorMatricula(@PathVariable String matricula) {
        Optional<AlunoModel> aluno = alunoService.buscarPorMatricula(matricula);
        return aluno.map(a -> ResponseEntity.ok(alunoService.converterParaDTO(a)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{matricula}/emprestimos")
    public ResponseEntity<List<EmprestimoDTO>> visualizarEmprestimos(@PathVariable String matricula) {
        List<EmprestimoDTO> emprestimos = alunoService.listarEmprestimos(matricula);
        if (emprestimos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/{matricula}/reservas")
    public ResponseEntity<ReservaDTO> visualizarReserva(@PathVariable String matricula) {
        Optional<ReservaDTO> reserva = alunoService.buscarReservaAtiva(matricula);
        return reserva.map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{matricula}/penalidades")
    public ResponseEntity<List<PenalidadeDTO>> visualizarPenalidades(@PathVariable String matricula) {
        List<PenalidadeDTO> penalidades = alunoService.listarPenalidades(matricula);
        if (penalidades.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(penalidades);
    }

    @GetMapping("/{matricula}/notificacoes")
    public ResponseEntity<List<NotificacaoDTO>> visualizarNotificacoes(@PathVariable String matricula) {
        List<NotificacaoDTO> notificacoes = alunoService.listarNotificacoes(matricula);
        if (notificacoes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/{matricula}/avaliacoes")
    public ResponseEntity<List<AvaliacaoDTO>> visualizarAvaliacoes(@PathVariable String matricula) {
        List<AvaliacaoDTO> avaliacoes = alunoService.listarAvaliacoes(matricula);
        if (avaliacoes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<AlunoDTO>> buscarPorNome(@PathVariable String nome) {
        List<AlunoModel> alunos = alunoService.buscarPorNome(nome);
        if (alunos.isEmpty()) return ResponseEntity.notFound().build();

        List<AlunoDTO> dtos = alunos.stream().map(a -> new AlunoDTO(
                a.getMatricula(),
                a.getNome(),
                a.getEmail(),
                a.getSenha(),
                a.getStatus()
        )).toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@Valid @RequestBody AlunoDTO dto) {
        if (alunoService.alunoExiste(dto.getMatricula())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um aluno com essa matrícula.");
        }

        AlunoModel novoAluno = alunoService.salvarDTO(dto);
        AlunoDTO resposta = alunoService.converterParaDTO(novoAluno);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody AlunoModel alunoLogin) {
        System.out.println("Matricula recebida: " + alunoLogin.getMatricula());  // Debug
        System.out.println("Senha recebida: " + alunoLogin.getSenha());  // Debug

        Optional<AlunoModel> alunoOpt = alunoService.autenticar(
                alunoLogin.getMatricula(),
                alunoLogin.getSenha()
        );

        if (alunoOpt.isPresent()) {
            AlunoModel aluno = alunoOpt.get();
            AlunoDTO dto = new AlunoDTO(
                    aluno.getMatricula(),
                    aluno.getNome(),
                    aluno.getEmail(),
                    aluno.getSenha(),
                    aluno.getStatus()
            );
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Matrícula ou senha inválida");
    }
}
