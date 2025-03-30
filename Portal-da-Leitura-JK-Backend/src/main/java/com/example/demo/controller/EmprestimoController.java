package com.example.demo.controller;

import com.example.demo.model.EmprestimoModel;
import com.example.demo.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<EmprestimoModel>> buscarPorAluno(@PathVariable Long alunoId) {
        List<EmprestimoModel> emprestimos = emprestimoService.buscarPorAluno(alunoId);
        return emprestimos.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(emprestimos);
    }

    @PostMapping
    public ResponseEntity<EmprestimoModel> salvar(@RequestBody EmprestimoModel emprestimo) {
        EmprestimoModel novoEmprestimo = emprestimoService.salvar(emprestimo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEmprestimo);
    }

    @DeleteMapping("/{emprestimoId}")
    public ResponseEntity<Void> devolverEmprestimo(@PathVariable Long emprestimoId) {
        emprestimoService.devolverEmprestimo(emprestimoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
