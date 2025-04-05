package com.example.demo.service;

import com.example.demo.model.NotificacaoModel;
import com.example.demo.repository.NotificacaoRepository;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private SendGrid sendGrid;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    // Buscar notificações por matrícula
    public List<NotificacaoModel> buscarPorAluno(Long matricula) {
        // Verificar se a matrícula existe
        if (matricula == null || matricula <= 0) {
            throw new IllegalArgumentException("Matrícula inválida");
        }
        return notificacaoRepository.findByMatricula(matricula);
    }

    // Buscar notificações por tipo
    public List<NotificacaoModel> buscarPorTipo(String tipo) {
        // Verificar se o tipo é válido
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de notificação inválido");
        }
        return notificacaoRepository.findByTipo(tipo);
    }

    // Salvar notificação no banco e enviar por e-mail
    public NotificacaoModel salvar(NotificacaoModel notificacao) {
        // Verificar se os campos obrigatórios estão presentes
        if (notificacao == null || notificacao.getAluno() == null || notificacao.getTipo() == null) {
            throw new IllegalArgumentException("Campos obrigatórios não preenchidos");
        }

        // Salvar a notificação no banco de dados
        NotificacaoModel savedNotificacao = notificacaoRepository.save(notificacao);

        try {
            // Enviar a notificação por e-mail
            sendEmail(savedNotificacao);
        } catch (IOException e) {
            // Logar a falha ao enviar e-mail
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            // Você pode lançar uma exceção personalizada aqui, se quiser lidar com falhas de envio de forma diferente
        }

        // Verificar se a notificação está relacionada a penalidades e aplicar se necessário
        verificarPenalidade(notificacao);

        return savedNotificacao;
    }

    // Enviar e-mail via SendGrid
    private void sendEmail(NotificacaoModel notificacao) throws IOException {
        // Criar o e-mail
        Email from = new Email(fromEmail);
        // Aqui é que ajustamos: pegar o e-mail do aluno
        Email to = new Email(notificacao.getAluno().getEmail());
        String subject = "Notificação: " + notificacao.getTipo();
        Content content = new Content("text/plain", notificacao.getMensagem());
        Mail mail = new Mail(from, subject, to, content);

        // Enviar o e-mail
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        // Enviar a requisição para o SendGrid
        Response response = sendGrid.api(request);
        if (response.getStatusCode() != 202) {
            throw new IOException("Falha ao enviar e-mail. Status: " + response.getStatusCode());
        }
    }

    // Métdo para verificar se a notificação envolve penalidade e tratá-la
    private void verificarPenalidade(NotificacaoModel notificacao) {
        // Lógica para penalidade - exemplo simples de penalidade por atraso
        if ("atraso".equals(notificacao.getTipo())) {
            // Aqui você pode aplicar uma penalidade, por exemplo, bloquear a conta por X dias
            System.out.println("Aplicando penalidade de atraso para o aluno: " + notificacao.getAluno().getMatricula());
            // Talvez criar um métoo para registrar a penalidade no banco de dados
        }
    }

    // Métdo para buscar uma notificação por ID
    public Optional<NotificacaoModel> buscarPorId(Long id) {
        return notificacaoRepository.findById(id);
    }
}
