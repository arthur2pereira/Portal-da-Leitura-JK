package com.example.demo.integration.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.demo.integration.dto.SendGridDTO;

import java.io.IOException;

@Service
public class SendGridService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendEmail(SendGridDTO sendgridDTO) throws IOException {
        Email from = new Email("seu-email@dominio.com");
        Email to = new Email(sendgridDTO.getEmailDestinatario());
        Content content = new Content("text/plain", sendgridDTO.getBodyContent());
        Mail mail = new Mail(from, sendgridDTO.getSubject(), to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw new IOException("Erro ao enviar e-mail", ex);
        }
    }
}
