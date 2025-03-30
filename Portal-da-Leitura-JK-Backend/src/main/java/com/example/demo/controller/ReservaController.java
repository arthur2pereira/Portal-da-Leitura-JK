package com.example.demo.controller;

import com.example.demo.model.ReservaModel;
import com.example.demo.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<ReservaModel>> buscarPorAluno(@PathVariable Long alunoId) {
        List<ReservaModel> reservas = reservaService.buscarPorAluno(alunoId);
        return reservas.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(reservas);
    }

    @PostMapping
    public ResponseEntity<ReservaModel> salvar(@RequestBody ReservaModel reserva) {
        ReservaModel novaReserva = reservaService.salvar(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReserva);
    }

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long reservaId) {
        reservaService.cancelarReserva(reservaId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
