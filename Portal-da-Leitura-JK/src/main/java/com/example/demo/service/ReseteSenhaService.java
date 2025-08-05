package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ReseteSenhaService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviar(String destino, String assunto, String conteudo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destino);
        mensagem.setSubject(assunto);
        mensagem.setText(conteudo);
        mailSender.send(mensagem);
    }
}
