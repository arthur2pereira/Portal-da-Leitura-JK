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
    public ResponseEntity<List<EmprestimoDTO>> buscarPorAluno(@PathVariable String matricula) {
        List<EmprestimoModel> emprestimos = emprestimoService.buscarPorAluno(matricula);
        return emprestimos.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(emprestimoService.converterParaDTO(emprestimos));
    }

    @GetMapping("/{emprestimoId}/atraso")
    public ResponseEntity<String> diasDeAtraso(@PathVariable Long emprestimoId) {
        int dias = emprestimoService.diasDeAtraso(emprestimoId);
        return ResponseEntity.ok("Dias de atraso: " + dias);
    }

    @PutMapping("/{emprestimoId}/renovar")
    public ResponseEntity<String> renovarEmprestimoAluno(@PathVariable Long emprestimoId, @RequestParam String matricula) {
        if (emprestimoService.renovarEmprestimoPorAluno(emprestimoId, matricula)) {
            return ResponseEntity.ok("Prazo renovado por +7 dias.");
        }
        return ResponseEntity.badRequest().body("Não foi possível renovar.");
    }

    @PutMapping("/{emprestimoId}/renovar-admin")
    public ResponseEntity<String> renovarEmprestimoBibliotecario(@PathVariable Long emprestimoId, @RequestParam int dias, @RequestParam String emailBibliotecario) {
        emprestimoService.renovarEmprestimoPorBibliotecario(emprestimoId, dias, emailBibliotecario);
        return ResponseEntity.ok("Prazo do empréstimo prorrogado pelo bibliotecário.");
    }

    @GetMapping("/{bibliotecarioId}/vencido")
    public ResponseEntity<Boolean> estaVencido(@PathVariable Long bibliotecarioId) {
        return ResponseEntity.ok(emprestimoService.estaVencido(bibliotecarioId));
    }

    @GetMapping("/bibliotecario/{bibliotecarioId}")
    public ResponseEntity<List<EmprestimoDTO>> buscarPorBibliotecario(@PathVariable Long bibliotecarioId) {
        List<EmprestimoModel> emprestimos = emprestimoService.buscarPorBibliotecario(bibliotecarioId);
        return emprestimos.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(emprestimoService.converterParaDTO(emprestimos));
    }

    @PostMapping("/registrar")
    public ResponseEntity<EmprestimoDTO> registrarEmprestimo(@RequestBody EmprestimoDTO dto) {
        EmprestimoModel emprestimo = emprestimoService.registrarEmprestimo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.converterParaDTO(emprestimo));
    }


    @PostMapping("/{emprestimoId}/devolver")
    public ResponseEntity<String> registrarDevolucao(@PathVariable Long emprestimoId) {
        emprestimoService.registrarDevolucao(emprestimoId);
        return ResponseEntity.ok("Devolução registrada com sucesso.");
    }
}
