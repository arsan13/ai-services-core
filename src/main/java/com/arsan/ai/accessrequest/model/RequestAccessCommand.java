package com.arsan.ai.accessrequest.model;

import com.arsan.ai.identity.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAccessCommand {

    private String requesterComment;
    private Set<RoleType> roles;
    private Set<String> permissions;
}
