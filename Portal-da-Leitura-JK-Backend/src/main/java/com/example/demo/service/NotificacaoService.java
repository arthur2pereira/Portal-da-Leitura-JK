package com.example.demo.service;

import com.example.demo.model.NotificacaoModel;
import com.example.demo.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public List<NotificacaoModel> buscarPorAluno(Long alunoId) {
        return notificacaoRepository.findByAlunoId(alunoId);
    }

    public List<NotificacaoModel> buscarPorTipo(String tipo) {
        return notificacaoRepository.findByTipo(tipo);
    }

    public NotificacaoModel salvar(NotificacaoModel notificacao) {
        return notificacaoRepository.save(notificacao);
    }
}
