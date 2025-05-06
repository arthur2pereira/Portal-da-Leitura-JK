package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.AlunoModel;
import com.example.demo.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService  alunoService;

    @GetMapping("/{matricula}")
    public ResponseEntity<AlunoDTO> buscarPorMatricula(@PathVariable String matricula) {
        Optional<AlunoModel> aluno = alunoService.buscarPorMatricula(matricula);
        return aluno.map(a -> ResponseEntity.ok(alunoService.converterParaDTO(a)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{matricula}/notificacoes")
    public ResponseEntity<List<NotificacaoDTO>> visualizarNotificacoes(@PathVariable String matricula) {
        List<NotificacaoDTO> notificacoes = alunoService.listarNotificacoes(matricula);
        if (notificacoes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/{matricula}/emprestimos")
    public ResponseEntity<List<EmprestimoDTO>> visualizarEmprestimos(@PathVariable String matricula) {
        List<EmprestimoDTO> emprestimos = alunoService.listarEmprestimos(matricula);
        int limite = 3;
        if (emprestimos.isEmpty()) return ResponseEntity.noContent().build();

        List<EmprestimoDTO> emprestimosLimitados = emprestimos.size() > limite ? emprestimos.subList(0, limite) : emprestimos;
        boolean temMais = emprestimos.size() > limite;

        return ResponseEntity.ok().header("X-Tem-Mais", String.valueOf(temMais)).body(emprestimosLimitados);
    }

    @GetMapping("/{matricula}/reservas")
    public ResponseEntity<List<ReservaDTO>> visualizarReservas(@PathVariable String matricula) {
        List<ReservaDTO> reservas = alunoService.listarReservas(matricula);
        int limite = 3;
        if (reservas.isEmpty()) return ResponseEntity.noContent().build();

        List<ReservaDTO> reservasLimitadas = reservas.size() > limite ? reservas.subList(0, limite) : reservas;
        boolean temMais = reservas.size() > limite;

        return ResponseEntity.ok().header("X-Tem-Mais", String.valueOf(temMais)).body(reservasLimitadas);
    }

    @GetMapping("/{matricula}/penalidades")
    public ResponseEntity<List<PenalidadeDTO>> visualizarPenalidades(@PathVariable String matricula) {
        List<PenalidadeDTO> penalidades = alunoService.listarPenalidades(matricula);
        int limite = 3;
        if (penalidades.isEmpty()) return ResponseEntity.noContent().build();

        List<PenalidadeDTO> penalidadesLimitadas = penalidades.size() > limite ? penalidades.subList(0, limite) : penalidades;
        boolean temMais = penalidades.size() > limite;

        return ResponseEntity.ok().header("X-Tem-Mais", String.valueOf(temMais)).body(penalidadesLimitadas);
    }

    @GetMapping("/{matricula}/avaliacoes")
    public ResponseEntity<List<AvaliacaoDTO>> visualizarAvaliacoes(@PathVariable String matricula) {
        List<AvaliacaoDTO> avaliacoes = alunoService.listarAvaliacoes(matricula);
        int limite = 3;
        if (avaliacoes.isEmpty()) return ResponseEntity.noContent().build();

        List<AvaliacaoDTO> avaliacoesLimitadas = avaliacoes.size() > limite ? avaliacoes.subList(0, limite) : avaliacoes;
        boolean temMais = avaliacoes.size() > limite;

        return ResponseEntity.ok().header("X-Tem-Mais", String.valueOf(temMais)).body(avaliacoesLimitadas);
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

    @DeleteMapping("/deletar")
    public ResponseEntity<String> deletarMinhaConta(Authentication authentication) {
        String matricula = authentication.getName();
        alunoService.deletarAlunoPorMatricula(matricula);
        return ResponseEntity.ok("Sua conta foi deletada com sucesso!");
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@Valid @RequestBody AlunoDTO dto) {
        if (alunoService.alunoExiste(dto.getMatricula())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um aluno com essa matrícula.");
        }

        try {
            AlunoModel novoAluno = alunoService.salvarDTO(dto);
            AlunoDTO resposta = alunoService.converterParaDTO(novoAluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar aluno: " + e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody AlunoModel alunoLogin) {
        System.out.println("Matrícula/Email recebidos: " + alunoLogin.getMatricula() + " / " + alunoLogin.getEmail());  // Debug
        System.out.println("Senha recebida: " + alunoLogin.getSenha());  // Debug

        Optional<TokenDTO> tokenOpt = alunoService.autenticar(
                alunoLogin.getMatricula(),
                alunoLogin.getEmail(),
                alunoLogin.getSenha()
        );

        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok(tokenOpt.get());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Matrícula/email ou senha inválidos");
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarAluno(@RequestBody AlunoDTO dto, Authentication authentication) {
        String matriculaLogada = authentication.getName();

        if (!dto.getMatricula().equals(matriculaLogada)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você só pode atualizar seus próprios dados.");
        }

        try {
            AlunoDTO atualizado = alunoService.atualizarAluno(dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

}
