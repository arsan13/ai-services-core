package com.arsan.chatbot.repository;

import com.arsan.chatbot.entity.TokenUsageAudit;
import com.arsan.chatbot.projection.UserTokenUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenUsageAuditRepository extends JpaRepository<TokenUsageAudit, Long> {

    List<TokenUsageAudit> findByUserId(String userId);

    List<TokenUsageAudit> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

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
             WHERE t.userId = :userId
               AND t.createdDate BETWEEN :start AND :end
            """)
    Long sumTotalTokensByUserIdAndCreatedDateBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
             SELECT t.userId as userId,
                    SUM(t.totalTokens) as totalTokens
             FROM TokenUsageAudit t
             WHERE t.createdDate BETWEEN :start AND :end
             GROUP BY t.userId
            """)
    List<UserTokenUsage> findUserTokenUsageByCreatedDateBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

//    Long sumTotalTokensByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
//    Long sumTotalTokensByUserIdAndCreatedDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
