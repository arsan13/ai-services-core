package com.arsan.ai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "token_usage_audit",
        indexes = {
                @Index(name = "idx_tua_user_created_id", columnList = "user_id, created_date, id"),
                @Index(name = "idx_tua_created_date_id", columnList = "created_date, id")
        }
)
@Immutable
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TokenUsageAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String model;
    private String provider;

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;

    private double costInUsd;
    private double latencySec;

    @Column(columnDefinition = "TEXT")
    private String inputSummary;

    @Column(columnDefinition = "TEXT")
    private String outputSummary;

    @CreationTimestamp
    private LocalDateTime createdDate;
}