package com.example.demo.controller;

import com.example.demo.model.AlunoModel;
import com.example.demo.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping("/{matricula}")
    public ResponseEntity<AlunoModel> buscarPorMatricula(@PathVariable String matricula) {
        Optional<AlunoModel> aluno = alunoService.buscarPorMatricula(matricula);
        return aluno.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<AlunoModel>> buscarPorNome(@PathVariable String nome) {
        List<AlunoModel> alunos = alunoService.buscarPorNome(nome);
        return alunos.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(alunos);
    }

    @PostMapping
    public ResponseEntity<AlunoModel> salvar(@RequestBody AlunoModel aluno) {
        AlunoModel novoAluno = alunoService.salvar(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
    }
}
