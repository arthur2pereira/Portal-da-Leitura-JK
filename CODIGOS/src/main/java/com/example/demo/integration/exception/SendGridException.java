package com.example.demo.integration.exception;

public class SendGridException extends RuntimeException {
    public SendGridException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public SendGridException(String mensagem) {
        super(mensagem);
    }
}


