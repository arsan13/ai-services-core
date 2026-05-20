package com.arsan.ai.accessrequest.mapper;

import com.arsan.ai.accessrequest.entity.AccessRequest;
import com.arsan.ai.accessrequest.enums.AccessRequestStatus;
import com.arsan.ai.accessrequest.model.RequestAccessCommand;
import org.springframework.stereotype.Component;

@Component
public class AccessRequestMapper {

    public AccessRequest toEntity(RequestAccessCommand dto) {
        return AccessRequest.builder()
                .requesterComment(dto.getRequesterComment())
                .roles(dto.getRoles())
                .permissions(dto.getPermissions())
                .status(AccessRequestStatus.PENDING)
                .build();
    }
}
