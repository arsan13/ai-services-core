package com.arsan.chatbot.controller.admin;

import com.arsan.chatbot.projection.TokenUsageAuditView;
import com.arsan.chatbot.projection.UserTokenUsage;
import com.arsan.chatbot.service.TokenUsageAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/token-usage")
@RequiredArgsConstructor
public class TokenUsageAuditController {

    private final TokenUsageAuditService service;

    @GetMapping
    public List<TokenUsageAuditView> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.getAll(page, size);
    }

    @GetMapping("/user/{userId}")
    public List<TokenUsageAuditView> getByUserId(@PathVariable Long userId) {
        return service.getByUserId(userId);
    }

    @GetMapping("/date-range")
    public List<TokenUsageAuditView> getByDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return service.getAuditsByDateRange(startDate, endDate);
    }

    @GetMapping("/total-tokens")
    public Long getTotalTokens(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return service.getTotalTokens(startDate, endDate);
    }

    @GetMapping("/total-tokens/user/{userId}")
    public Long getTotalTokensByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return service.getTotalTokensByUser(userId, startDate, endDate);
    }

    @GetMapping("/summary")
    public List<UserTokenUsage> getUserTokenUsageSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return service.getUserTokenUsageSummary(startDate, endDate);
    }
}
