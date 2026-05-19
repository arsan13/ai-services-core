package com.arsan.ai.profile.mapper;

import com.arsan.ai.accessrequest.entity.AccessRequest;
import com.arsan.ai.accessrequest.model.RequestAccessCommand;
import com.arsan.ai.profile.model.AccessRequestCreateDto;
import com.arsan.ai.profile.model.AccessRequestResponseDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserAccessRequestMapper {

    public RequestAccessCommand toRequestCommand(AccessRequestCreateDto dto) {
        return RequestAccessCommand.builder()
                .requesterComment(dto.getRequesterComment())
                .roles(dto.getRoles())
                .permissions(dto.getPermissions())
                .build();
    }

    public AccessRequestResponseDto toResponseDto(AccessRequest entity) {
        return AccessRequestResponseDto.builder()
                .id(entity.getId())
                .reviewerName(entity.getReviewer() != null ? entity.getReviewer().getFullName() : null)
                .status(entity.getStatus())
                .requesterComment(entity.getRequesterComment())
                .reviewerComment(entity.getReviewerComment())
                .roles(Set.copyOf(entity.getRoles()))
                .permissions(Set.copyOf(entity.getPermissions()))
                .requestedDate(entity.getRequestedDate())
                .reviewedDate(entity.getReviewedDate())
                .build();
    }
}
