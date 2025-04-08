package com.example.demo.controller;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.LoginDTO;
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
        return aluno.map(a -> ResponseEntity.ok(new AlunoDTO(
                a.getMatricula(),
                a.getNome(),
                a.getEmail(),
                a.getSenha(),
                a.getDataNascimento(),
                a.getStatus() ? "Ativo" : "Inativo"
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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
                a.getDataNascimento(),
                a.getStatus() ? "Ativo" : "Inativo"
        )).toList();

        return ResponseEntity.ok(dtos);
    }
    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody AlunoDTO dto) {
        if (alunoService.alunoExiste(dto.getMatricula())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um aluno com essa matrícula.");
        }

        AlunoModel novoAluno = alunoService.salvarDTO(dto);
        AlunoDTO resposta = alunoService.converterParaDTO(novoAluno);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<AlunoModel> alunoOpt = alunoService.autenticar(
                loginDTO.getMatricula(),
                loginDTO.getSenha()
        );

        if (alunoOpt.isPresent()) {
            AlunoModel aluno = alunoOpt.get();
            AlunoDTO dto = new AlunoDTO(
                    aluno.getMatricula(),
                    aluno.getNome(),
                    aluno.getEmail(),
                    aluno.getSenha(),
                    aluno.getDataNascimento(),
                    aluno.getStatus() ? "Ativo" : "Inativo"
            );
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Matrícula ou senha inválida");
    }

}
