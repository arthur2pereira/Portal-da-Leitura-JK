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
    public ResponseEntity<List<NotificacaoModel>> buscarPorAluno(@PathVariable Long matricula) {
        List<NotificacaoModel> notificacoes = notificacaoService.buscarPorAluno(matricula);
        return notificacoes.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(notificacoes);
    }

    @PostMapping
    public ResponseEntity<NotificacaoModel> salvar(@RequestBody NotificacaoModel notificacao) {
        NotificacaoModel novaNotificacao = notificacaoService.salvar(notificacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaNotificacao);
    }
}
