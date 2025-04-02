package com.example.demo.controller;

import com.example.demo.model.PenalidadeModel;
import com.example.demo.service.PenalidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/penalidades")
public class PenalidadeController {

    @Autowired
    private PenalidadeService penalidadeService;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<PenalidadeModel>> buscarPorAluno(@PathVariable Long alunoId) {
        List<PenalidadeModel> penalidades = penalidadeService.buscarPorAluno(alunoId);
        return penalidades.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(penalidades);
    }

    @PostMapping
    public ResponseEntity<PenalidadeModel> salvar(@RequestBody PenalidadeModel penalidade) {
        PenalidadeModel novaPenalidade = penalidadeService.salvar(penalidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPenalidade);
    }
}
