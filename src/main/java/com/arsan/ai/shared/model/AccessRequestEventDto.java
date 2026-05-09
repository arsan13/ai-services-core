package com.arsan.ai.shared.model;

import com.arsan.ai.shared.enums.AccessRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestEventDto {

    private Long requestId;

    private Long requesterId;
    private Long reviewerId;

    private AccessRequestStatus status;

    private Set<String> roles;
    private Set<String> permissions;

    private String action; // CREATED, APPROVED, REJECTED

    private long timestamp;
}
