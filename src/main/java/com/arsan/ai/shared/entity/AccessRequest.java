package com.arsan.ai.shared.entity;

import com.arsan.ai.shared.enums.AccessRequestStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "access_request", indexes = {
        @Index(name = "idx_access_request_requester", columnList = "requester_id"),
        @Index(name = "idx_access_request_reviewer", columnList = "reviewer_id"),
        @Index(name = "idx_access_request_status", columnList = "status"),
        @Index(name = "idx_access_request_requested_date", columnList = "requested_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private AppUser requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private AppUser reviewer;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessRequestStatus status = AccessRequestStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String requesterComment;

    @Column(columnDefinition = "TEXT")
    private String reviewerComment;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime requestedDate;

    private LocalDateTime reviewedDate;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ElementCollection
    @CollectionTable(name = "access_request_roles", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "role")
    private Set<String> roles;

    @ElementCollection
    @CollectionTable(name = "access_request_permissions", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "permission")
    private Set<String> permissions;
}
