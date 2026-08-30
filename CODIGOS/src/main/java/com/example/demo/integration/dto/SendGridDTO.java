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
	public String getEmailDestinatario() {
		return null;
	}
	public String getBodyContent() {
		return null;
	}
	public String getSubject() {
		return null;
	}
}
