package com.arsan.ai.profile.model;

import com.arsan.ai.accessrequest.enums.AccessRequestStatus;
import com.arsan.ai.auth.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestResponseDto {

    private Long id;

    private String reviewerName;

    private AccessRequestStatus status;

    private String requesterComment;
    private String reviewerComment;

    private Set<RoleType> roles;
    private Set<String> permissions;

    private LocalDateTime requestedDate;
    private LocalDateTime reviewedDate;
}
