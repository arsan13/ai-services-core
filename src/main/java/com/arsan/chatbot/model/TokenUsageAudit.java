package com.arsan.chatbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenUsageAudit {

    private UUID id;
    private String userId;

    // Model details
    private String model;
    private String provider;

    // Token usage
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;

    // Cost (optional)
    private double costInUSD;

    // Metadata
    private String inputSummary;
    private String outputSummary;

    // Timing
    private LocalDateTime createdAt;
    private double latencySec;

    // Optional factory method for defaults
    public static TokenUsageAudit create() {
        return TokenUsageAudit.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();
    }
}