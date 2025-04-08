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

    @GetMapping("/aluno/{matricula}")
    public ResponseEntity<List<EmprestimoModel>> buscarPorAluno(@PathVariable String matricula) {
        List<EmprestimoModel> emprestimos = emprestimoService.buscarPorAluno(matricula);
        return emprestimos.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(emprestimos);
    }

    // Endpoint pro ALUNO renovar (só 1x)
    @PutMapping("/{id}/renovar")
    public ResponseEntity<String> renovarEmprestimoAluno(
            @PathVariable Long id,
            @RequestParam String matricula
    ) {
        emprestimoService.renovarEmprestimoPorAluno(id, matricula);
        return ResponseEntity.ok("Empréstimo renovado com sucesso por mais 7 dias.");
    }

    // Endpoint pro BIBLIOTECÁRIO renovar manualmente
    @PutMapping("/{id}/renovar-admin")
    public ResponseEntity<String> renovarEmprestimoBibliotecario(
            @PathVariable Long id,
            @RequestParam int dias,
            @RequestParam String emailBibliotecario
    ) {
        emprestimoService.renovarEmprestimoPorBibliotecario(id, dias, emailBibliotecario);
        return ResponseEntity.ok("Prazo do empréstimo prorrogado pelo bibliotecário.");
    }

    @GetMapping("/bibliotecario/{bibliotecarioId}")
    public ResponseEntity<List<EmprestimoModel>> buscarPorBibliotecario(@PathVariable Long bibliotecarioId) {
        List<EmprestimoModel> emprestimos = emprestimoService.buscarPorBibliotecario(bibliotecarioId);
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
