package com.example.demo.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendGridDTO {
    private String subject;
    private String bodyContent;
    private String emailDestinatario;
}
