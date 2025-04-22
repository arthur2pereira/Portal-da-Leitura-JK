package com.example.demo.controller;

import com.example.demo.model.AvaliacaoModel;
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

    @GetMapping("/aluno/{matricula}")
    public ResponseEntity<List<PenalidadeModel>> buscarPorAluno(@PathVariable String matricula) {
        List<PenalidadeModel> penalidades = penalidadeService.buscarPorAluno(matricula);
        return penalidades.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(penalidades);
    }

    @PostMapping("/aplicar")
    public ResponseEntity<?> aplicar(@RequestParam String matricula,
                                     @RequestParam String motivo,
                                     @RequestParam String tipo,
                                     @RequestParam Integer diasBloqueio,
                                     @RequestParam String email) {
        try {
            PenalidadeModel p = penalidadeService.aplicarPenalidade(matricula, motivo, tipo, diasBloqueio, email);
            return ResponseEntity.ok(p);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{penalidadeId}/ativa")
    public ResponseEntity<?> estaAtiva(@PathVariable Long penalidadeId) {
        try {
            boolean ativa = penalidadeService.estaAtiva(penalidadeId);
            return ResponseEntity.ok(ativa);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{penalidadeId}/diasRestantes")
    public ResponseEntity<?> diasRestantes(@PathVariable Long penalidadeId) {
        try {
            int dias = penalidadeService.diasRestantes(penalidadeId);
            return ResponseEntity.ok("Dias restantes de penalidade: " + dias);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/avaliacoes/{matricula}")
    public ResponseEntity<?> listarAvaliacoes(@PathVariable String matricula) {
        try {
            List<AvaliacaoModel> avaliacoes = penalidadeService.listarAvaliacoes(matricula);
            return ResponseEntity.ok(avaliacoes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
