package com.arsan.chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Table(name = "token_usage_audit")
@Entity
public class TokenUsageAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;

    private String model;
    private String provider;

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;

    private double costInUSD;
    private double latencySec;

    @Lob
    @ToString.Exclude
    private String inputSummary;

    @Lob
    @ToString.Exclude
    private String outputSummary;

    @CreationTimestamp
    private LocalDateTime createdAt;
}