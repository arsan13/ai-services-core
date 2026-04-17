package com.arsan.chatbot.repository;

import com.arsan.chatbot.entity.TokenUsageAudit;
import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.projection.TokenUsageAuditView;
import com.arsan.chatbot.projection.UserTokenUsage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenUsageAuditRepository extends JpaRepository<TokenUsageAudit, Long> {

    List<TokenUsageAuditView> findAllBy(Pageable pageable);

    List<TokenUsageAuditView> findByUserId(Long userId);

    List<TokenUsageAuditView> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT COALESCE(SUM(t.totalTokens), 0)
                FROM TokenUsageAudit t
                WHERE t.createdDate BETWEEN :start AND :end
            """)
    Long sumTotalTokensByCreatedDateBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
                SELECT COALESCE(SUM(t.totalTokens), 0)
                FROM TokenUsageAudit t
                WHERE t.user = :user
                  AND t.createdDate BETWEEN :start AND :end
            """)
    Long sumTotalTokensByUserAndCreatedDateBetween(
            @Param("user") User user,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
                SELECT t.user.id as userId,
                       SUM(t.totalTokens) as totalTokens,
                       SUM(t.costInUsd) as costInUsd
                FROM TokenUsageAudit t
                WHERE t.createdDate BETWEEN :start AND :end
                GROUP BY t.user.id
            """)
    List<UserTokenUsage> findUserTokenUsageByCreatedDateBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
