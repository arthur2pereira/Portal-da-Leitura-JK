package com.example.demo.integration.exception;

public class BooksException extends RuntimeException {
    public BooksException(String mensagem) {
        super(mensagem);
    }
}
