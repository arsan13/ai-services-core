package com.arsan.chatbot.advisor;

import com.arsan.chatbot.model.TokenUsageAudit;
import com.arsan.chatbot.util.OpenAiCostCalculator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class TokenUsageAdvisor implements CallAdvisor {

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        long startTime = System.currentTimeMillis();
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        long latencyMs = (System.currentTimeMillis() - startTime);

        ChatResponse chatResponse = chatClientResponse.chatResponse();

        TokenUsageAudit audit = TokenUsageAudit.create();
        audit.setUserId("default-user");
        audit.setLatencySec(TimeUnit.MILLISECONDS.toSeconds(latencyMs));
        audit.setProvider("openai");

        if (chatResponse != null) {
            audit.setModel(chatResponse.getMetadata().getModel());

            Usage usage = chatResponse.getMetadata().getUsage();
            audit.setPromptTokens(usage.getPromptTokens());
            audit.setCompletionTokens(usage.getCompletionTokens());
            audit.setTotalTokens(usage.getTotalTokens());

            double cost = OpenAiCostCalculator.calculateCost(audit.getModel(), audit.getPromptTokens(), audit.getCompletionTokens());
            audit.setCostInUSD(cost);
        }

        audit.setInputSummary(chatClientRequest.prompt().getUserMessage().getText());
        if (chatResponse != null && chatResponse.getResult() != null) {
            audit.setOutputSummary(chatResponse.getResult().getOutput().getText());
        }

        saveAudit(audit);

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "token-usage-advisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Async
    private void saveAudit(TokenUsageAudit audit) {
        // TODO: Persist audit to DB
        System.out.printf("PromptTokens=%d, CompletionTokens=%d, TotalTokens=%d, Cost=%.6f USD%n",
                audit.getPromptTokens(), audit.getCompletionTokens(), audit.getTotalTokens(), audit.getCostInUSD());
    }

}
