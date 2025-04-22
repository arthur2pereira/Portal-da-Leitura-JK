package com.example.demo.controller;

import com.example.demo.dto.EmprestimoDTO;
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

    @GetMapping("/{emprestimoId}/atraso")
    public ResponseEntity<?> diasDeAtraso(@PathVariable Long emprestimoId) {
        try {
            int dias = emprestimoService.diasDeAtraso(emprestimoId);
            return ResponseEntity.ok("Dias de atraso: " + dias);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{emprestimoId}/renovar")
    public ResponseEntity<?> renovarEmprestimoAluno(@PathVariable Long emprestimoId, @RequestParam String matricula) {
        try {
            boolean renovado = emprestimoService.renovarEmprestimoPorAluno(emprestimoId, matricula);
            return renovado ? ResponseEntity.ok("Prazo renovado por +7 dias.")
                    : ResponseEntity.badRequest().body("Não foi possível renovar.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{emprestimoId}/renovar-admin")
    public ResponseEntity<String> renovarEmprestimoBibliotecario(
            @PathVariable Long emprestimoId,
            @RequestParam int dias,
            @RequestParam String emailBibliotecario
    ) {
        emprestimoService.renovarEmprestimoPorBibliotecario(emprestimoId, dias, emailBibliotecario);
        return ResponseEntity.ok("Prazo do empréstimo prorrogado pelo bibliotecário.");
    }

    @GetMapping("/{bibliotecarioId}/vencido")
    public ResponseEntity<Boolean> estaVencido(@PathVariable Long bibliotecarioId) {
        try {
            return ResponseEntity.ok(emprestimoService.estaVencido(bibliotecarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/bibliotecario/{bibliotecarioId}")
    public ResponseEntity<List<EmprestimoModel>> buscarPorBibliotecario(@PathVariable Long bibliotecarioId) {
        List<EmprestimoModel> emprestimos = emprestimoService.buscarPorBibliotecario(bibliotecarioId);
        return emprestimos.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(emprestimos);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarEmprestimo(@RequestBody EmprestimoDTO dto) {
        try {
            EmprestimoModel e = emprestimoService.registrarEmprestimo(dto.getMatricula(), dto.getLivroId(), dto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.converterParaDTO(e));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{emprestimoId}/devolver")
    public ResponseEntity<?> registrarDevolucao(@PathVariable Long emprestimoId) {
        try {
            emprestimoService.registrarDevolucao(emprestimoId);
            return ResponseEntity.ok("Devolução registrada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
