package com.arsan.ai.shared.model;

import com.arsan.ai.auth.enums.RoleType;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestSummaryDto {

    private Long id;

    private Long requesterId;
    private String requesterName;

    private Long reviewerId;
    private String reviewerName;

    private AccessRequestStatus status;

    private String requesterComment;
    private String reviewerComment;

    private Set<RoleType> roles;
    private Set<String> permissions;

    private LocalDateTime requestedDate;
    private LocalDateTime reviewedDate;
}
