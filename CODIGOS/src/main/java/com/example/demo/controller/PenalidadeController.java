package com.example.demo.controller;

import com.example.demo.dto.PenalidadeDTO;
import com.example.demo.model.AvaliacaoModel;
import com.example.demo.model.PenalidadeModel;
import com.example.demo.service.PenalidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/penalidades")
public class PenalidadeController {

    @Autowired
    private PenalidadeService penalidadeService;

    @GetMapping("/aluno/{matricula}")
    public ResponseEntity<List<PenalidadeDTO>> buscarPorAluno(@PathVariable String matricula) {
        List<PenalidadeModel> penalidades = penalidadeService.buscarPorAluno(matricula);
        if (penalidades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            List<PenalidadeDTO> penalidadesDTO = penalidades.stream()
                    .map(this::converterDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(penalidadesDTO);
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<PenalidadeDTO>> buscarPorTipo(@PathVariable String tipo) {
        List<PenalidadeModel> penalidades = penalidadeService.buscarPorTipo(tipo);
        if (penalidades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            List<PenalidadeDTO> penalidadesDTO = penalidades.stream()
                    .map(this::converterDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(penalidadesDTO);
        }
    }

    @PostMapping("/aplicar")
    public ResponseEntity<?> aplicar(@RequestParam String matricula,
                                     @RequestParam String motivo,
                                     @RequestParam String tipo,
                                     @RequestParam Integer diasBloqueio,
                                     @RequestParam String email) {
        try {
            PenalidadeModel penalidade = penalidadeService.aplicarPenalidade(matricula, motivo, tipo, diasBloqueio, email);
            PenalidadeDTO penalidadeDTO = converterDTO(penalidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(penalidadeDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{penalidadeId}/ativa")
    public ResponseEntity<?> estaAtiva(@PathVariable Long penalidadeId) {
        try {
            boolean ativa = penalidadeService.estaAtiva(penalidadeId);
            return ResponseEntity.ok(ativa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{penalidadeId}/diasRestantes")
    public ResponseEntity<?> diasRestantes(@PathVariable Long penalidadeId) {
        try {
            int dias = penalidadeService.diasRestantes(penalidadeId);
            return ResponseEntity.ok("Dias restantes de penalidade: " + dias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/avaliacoes/{matricula}")
    public ResponseEntity<?> listarAvaliacoes(@PathVariable String matricula) {
        try {
            List<AvaliacaoModel> avaliacoes = penalidadeService.listarAvaliacoes(matricula);
            return ResponseEntity.ok(avaliacoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private PenalidadeDTO converterDTO(PenalidadeModel penalidade) {
        return new PenalidadeDTO(
                penalidade.getPenalidadeId(),
                penalidade.getAluno().getMatricula(),
                penalidade.getMotivo(),
                penalidade.getTipo(),
                penalidade.getDataAplicacao(),
                penalidade.getDiasBloqueio()
        );
    }
}
