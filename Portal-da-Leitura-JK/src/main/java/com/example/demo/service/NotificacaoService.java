package com.example.demo.service;

import com.example.demo.model.AlunoModel;
import com.example.demo.model.NotificacaoModel;
import com.example.demo.repository.NotificacaoRepository;
import com.example.demo.repository.AlunoRepository;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
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

    // Buscar notificações por matrícula
    public List<NotificacaoModel> buscarPorAluno(String matricula) {
        validarAlunoExistente(matricula);
        return notificacaoRepository.findByAlunoMatricula(matricula);
    }

    // Buscar notificações não lidas
    public List<NotificacaoModel> buscarNaoLidasPorAluno(String matricula) {
        validarAlunoExistente(matricula);
        return notificacaoRepository.findByAlunoMatriculaAndLidaFalse(matricula);
    }

    // Buscar por tipo e matrícula
    public List<NotificacaoModel> buscarPorTipoEMatricula(String tipo, String matricula) {
        validarAlunoExistente(matricula);
        return notificacaoRepository.findByTipoAndAlunoMatricula(tipo, matricula);
    }

    // Buscar notificações recentes
    public List<NotificacaoModel> buscarAposDataPorAluno(LocalDate data, String matricula) {
        validarAlunoExistente(matricula);
        return notificacaoRepository.findByDataEnvioAfterAndAlunoMatricula(data, matricula);
    }

    // Buscar por ID
    public Optional<NotificacaoModel> buscarPorId(Long id) {
        return notificacaoRepository.findById(id);
    }

    // Marcar como lida
    public NotificacaoModel marcarComoLida(Long id) {
        NotificacaoModel notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada."));
        notificacao.setLida(true);
        return notificacaoRepository.save(notificacao);
    }

    // Salvar e enviar
    public NotificacaoModel salvar(NotificacaoModel notificacao) {
        validarNotificacao(notificacao);
        validarAlunoExistente(notificacao.getAluno().getMatricula());

        if (notificacao.getDataEnvio() == null) {
            notificacao.setDataEnvio(LocalDate.now());
        }

        NotificacaoModel salva = notificacaoRepository.save(notificacao);

        try {
            sendEmail(salva);
        } catch (IOException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }

        verificarPenalidade(salva);
        return salva;
    }

    // Deletar notificação
    public void deletar(Long id) {
        if (!notificacaoRepository.existsById(id)) {
            throw new IllegalArgumentException("Notificação com ID " + id + " não existe.");
        }
        notificacaoRepository.deleteById(id);
    }

    // ========= MÉTODOS PRIVADOS ========= //

    private void validarNotificacao(NotificacaoModel notificacao) {
        if (notificacao == null ||
                notificacao.getAluno() == null ||
                notificacao.getAluno().getMatricula() == null ||
                notificacao.getTipo() == null ||
                notificacao.getMensagem() == null ||
                notificacao.getMensagem().isBlank()) {
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
        if ("atraso".equalsIgnoreCase(notificacao.getTipo())) {
            System.out.println("Aplicar penalidade para matrícula: " + notificacao.getAluno().getMatricula());
            // Chamar serviço de penalidade aqui se tiver
            // penalidadeService.aplicarPenalidade(notificacao.getAluno(), tipo, diasBloqueio);
        }
    }

    public List<NotificacaoModel> buscarPorAlunoMaisRecentes(String matricula) {
        validarAlunoExistente(matricula);
        return notificacaoRepository.findByAlunoMatriculaOrderByDataEnvioDesc(matricula);
    }

}
