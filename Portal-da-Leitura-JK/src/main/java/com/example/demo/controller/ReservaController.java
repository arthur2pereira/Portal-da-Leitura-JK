package com.example.demo.controller;

import com.example.demo.dto.ReservaDTO;
import com.example.demo.model.ReservaModel;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/aluno/{matricula}")
    public ResponseEntity<List<ReservaModel>> buscarPorAluno(@PathVariable String matricula) {
        List<ReservaModel> reservas = reservaService.buscarPorAluno(matricula);
        return reservas.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(reservas);
    }

    @GetMapping("/{reservaId}/ativa")
    public ResponseEntity<Boolean> estaAtiva(@PathVariable Long reservaId) {
        Optional<ReservaModel> reservaOpt = reservaRepository.findByReservaId(reservaId);
        if (reservaOpt.isEmpty()) return ResponseEntity.notFound().build();

        ReservaModel reserva = reservaOpt.get();
        return ResponseEntity.ok(reservaService.estaAtiva(reservaId));
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarReserva(@RequestBody ReservaDTO dto) {
        try {
            ReservaModel reserva = reservaService.criarReserva(dto.getMatricula(), dto.getLivroId());
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.converterParaDTO(reserva));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{reservaId}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long reservaId) {
        try {
            reservaService.cancelarReserva(reservaId);
            return ResponseEntity.ok("Reserva cancelada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{reservaId}/validade")
    public ResponseEntity<String> verificarValidade(@PathVariable Long reservaId) {
        Optional<ReservaModel> reservaOpt = reservaRepository.findByReservaId(reservaId);
        if (reservaOpt.isEmpty()) return ResponseEntity.notFound().build();

        ReservaModel reserva = reservaOpt.get();
        LocalDate hoje = LocalDate.now();
        if (hoje.isAfter(reserva.getDataVencimento())) {
            return ResponseEntity.ok("Reserva vencida.");
        } else {
            long diasRestantes = ChronoUnit.DAYS.between(hoje, reserva.getDataVencimento());
            return ResponseEntity.ok("Reserva válida por mais " + diasRestantes + " dias.");
        }
    }
}
