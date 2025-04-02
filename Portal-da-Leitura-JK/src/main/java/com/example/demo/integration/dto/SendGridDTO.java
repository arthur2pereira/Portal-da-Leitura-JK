package com.example.demo.integration.dto;

public class SendGridDTO {
    private String subject;
    private String bodyContent;
    private String emailDestinatario;

    public SendGridDTO() {
    }

    public SendGridDTO(String subject, String bodyContent, String emailDestinatario) {
        this.subject = subject;
        this.bodyContent = bodyContent;
        this.emailDestinatario = emailDestinatario;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    public String getEmailDestinatario() {
        return emailDestinatario;
    }

    public void setEmailDestinatario(String emailDestinatario) {
        this.emailDestinatario = emailDestinatario;
    }
}
