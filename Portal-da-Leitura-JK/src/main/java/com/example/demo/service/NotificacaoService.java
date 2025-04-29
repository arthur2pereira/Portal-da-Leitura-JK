package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.dto.*;
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
    private AlunoRepository alunoRepository;

    @Autowired
    private SendGrid sendGrid;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public List<NotificacaoDTO> buscarPorAluno(String matricula) {
        validarAlunoExistente(matricula);
        List<NotificacaoModel> notificacoes = notificacaoRepository.findByAlunoMatricula(matricula);
        return notificacaoModelDTO(notificacoes);
    }

    public List<NotificacaoDTO> buscarNaoLidasPorAluno(String matricula) {
        validarAlunoExistente(matricula);
        List<NotificacaoModel> notificacoes = notificacaoRepository.findByAlunoMatriculaAndLidaFalse(matricula);
        return notificacaoModelDTO(notificacoes);
    }

    public NotificacaoDTO marcarComoLida(Long notificacaoId) {
        NotificacaoModel notificacao = notificacaoRepository.findByNotificacaoId(notificacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada."));
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
        return converterParaDTO(notificacao);
    }

    public NotificacaoDTO salvar(NotificacaoDTO notificacaoRequest) {
        validarNotificacao(notificacaoRequest);
        validarAlunoExistente(notificacaoRequest.getMatricula());

        AlunoModel aluno = alunoRepository.findByMatricula(notificacaoRequest.getMatricula())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setAluno(aluno);
        notificacao.setTipo(notificacaoRequest.getTipo());
        notificacao.setMensagem(notificacaoRequest.getMensagem());

        NotificacaoModel salva = notificacaoRepository.save(notificacao);

        try {
            sendEmail(salva);
        } catch (IOException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }

        verificarPenalidade(salva);
        return converterParaDTO(salva);
    }

    private void validarNotificacao(NotificacaoDTO notificacaoRequest) {
        if (notificacaoRequest == null ||
                notificacaoRequest.getMatricula() == null ||
                notificacaoRequest.getTipo() == null ||
                notificacaoRequest.getMensagem() == null ||
                notificacaoRequest.getMensagem().isBlank()) {
            throw new IllegalArgumentException("Campos obrigatórios da notificação não preenchidos.");
        }
    }


    private void validarAlunoExistente(String matricula) {
        boolean existe = alunoRepository.existsByMatricula(matricula);
        if (!existe) {
            throw new IllegalArgumentException("Aluno com matrícula " + matricula + " não encontrado.");
        }
    }

    private void sendEmail(NotificacaoModel notificacao) throws IOException {
        Email from = new Email(fromEmail);
        Email to = new Email(notificacao.getAluno().getEmail());
        String subject = "Notificação: " + notificacao.getTipo();
        Content content = new Content("text/plain", notificacao.getMensagem());

        Mail mail = new Mail(from, subject, to, content);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);
        if (response.getStatusCode() != 202) {
            throw new IOException("Falha ao enviar e-mail. Status: " + response.getStatusCode());
        }
    }

    private void verificarPenalidade(NotificacaoModel notificacao) {
        if ("ALERTA".equals(notificacao.getTipo())) {
            System.out.println("Aplicar penalidade para matrícula: " + notificacao.getAluno().getMatricula());
        }
    }

    public boolean estaLida(Long notificacaoId) {
        NotificacaoModel notificacao = notificacaoRepository.findByNotificacaoId(notificacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada."));
        return notificacao.isLida();
    }

    private NotificacaoDTO converterParaDTO(NotificacaoModel notificacao) {
        return new NotificacaoDTO(
                notificacao.getNotificacaoId(),
                notificacao.getAluno().getMatricula(),
                notificacao.getMensagem(),
                notificacao.getTipo(),
                notificacao.isLida()
        );
    }

    private List<NotificacaoDTO> notificacaoModelDTO(List<NotificacaoModel> notificacoes) {
        return notificacoes.stream()
                .map(this::converterParaDTO)
                .toList();
    }
}
