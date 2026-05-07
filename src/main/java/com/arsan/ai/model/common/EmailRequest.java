package com.arsan.ai.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private List<String> to;
    private String subject;
    private String htmlBody;

    private List<String> cc;
    private List<String> bcc;

    public EmailRequest(List<String> to, String subject, String htmlBody) {
        this.to = to;
        this.subject = subject;
        this.htmlBody = htmlBody;
    }
}