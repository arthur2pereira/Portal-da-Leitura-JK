package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping("/aluno/{matricula}")
    public ResponseEntity<List<NotificacaoDTO>> buscarPorAluno(@PathVariable String matricula) {
        try {
            List<NotificacaoDTO> notificacoes = notificacaoService.buscarPorAluno(matricula);
            if (notificacoes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notificacoes);
            }
            return ResponseEntity.ok(notificacoes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/aluno/{matricula}/nao-lidas")
    public ResponseEntity<List<NotificacaoDTO>> buscarNaoLidas(@PathVariable String matricula) {
        try {
            List<NotificacaoDTO> notificacoes = notificacaoService.buscarNaoLidasPorAluno(matricula);
            return notificacoes.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                    : ResponseEntity.ok(notificacoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bibliotecario/{matricula}/nao-lidas")
    public ResponseEntity<List<NotificacaoDTO>> buscarNaoLidasPorBibliotecario(@PathVariable Long bibliotecarioId) {
        try {
            List<NotificacaoDTO> notificacoes = notificacaoService.buscarNaoLidasPorBibliotecario(bibliotecarioId);
            return notificacoes.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                    : ResponseEntity.ok(notificacoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{notificacaoId}/marcar-como-lida")
    public ResponseEntity<NotificacaoDTO> marcarComoLida(@PathVariable Long notificacaoId) {
        try {
            NotificacaoDTO notificacaoDTO = notificacaoService.marcarComoLida(notificacaoId);
            return ResponseEntity.ok(notificacaoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<NotificacaoDTO> criarNotificacao(@RequestBody NotificacaoDTO notificacaoRequest) {
        try {
            NotificacaoDTO notificacaoDTO = notificacaoService.salvar(notificacaoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacaoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{notificacaoId}/esta-lida")
    public ResponseEntity<Boolean> estaLida(@PathVariable Long notificacaoId) {
        try {
            boolean lida = notificacaoService.estaLida(notificacaoId);
            return ResponseEntity.ok(lida);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
