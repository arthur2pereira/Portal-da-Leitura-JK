package com.example.demo.controller;

import com.example.demo.model.NotificacaoModel;
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
    public ResponseEntity<List<NotificacaoModel>> buscarPorAluno(@PathVariable String matricula) {
        try {
            List<NotificacaoModel> notificacoes = notificacaoService.buscarPorAluno(matricula);
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

    @PostMapping
    public ResponseEntity<NotificacaoModel> salvar(@RequestBody NotificacaoModel notificacao) {
        try {
            NotificacaoModel novaNotificacao = notificacaoService.salvar(notificacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaNotificacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/aluno/{matricula}/nao-lidas")
    public ResponseEntity<List<NotificacaoModel>> buscarNaoLidas(@PathVariable String matricula) {
        try {
            List<NotificacaoModel> notificacoes = notificacaoService.buscarNaoLidasPorAluno(matricula);
            return notificacoes.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                    : ResponseEntity.ok(notificacoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
