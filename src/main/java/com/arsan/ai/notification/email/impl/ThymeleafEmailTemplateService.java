package com.arsan.ai.notification.email.impl;

import com.arsan.ai.notification.email.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThymeleafEmailTemplateService implements EmailTemplateService {

    private static final String EMAIL_TEMPLATE_BASE_PATH = "email/";

    private final SpringTemplateEngine templateEngine;

    public String render(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariable("appName", "The AI Services");
        context.setVariables(variables);

        return templateEngine.process(EMAIL_TEMPLATE_BASE_PATH + templateName, context);
    }
}
