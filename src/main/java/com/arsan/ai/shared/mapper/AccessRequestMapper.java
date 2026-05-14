package com.arsan.ai.shared.mapper;

import com.arsan.ai.admin.model.AccessRequestSummaryDto;
import com.arsan.ai.profile.model.AccessRequestCreateDto;
import com.arsan.ai.shared.entity.AccessRequest;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.profile.model.AccessRequestResponseDto;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AccessRequestMapper {

    public AccessRequest toEntity(AccessRequestCreateDto dto) {
        return AccessRequest.builder()
                .requesterComment(dto.getRequesterComment())
                .roles(dto.getRoles())
                .permissions(dto.getPermissions())
                .status(AccessRequestStatus.PENDING)
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

    public AccessRequestSummaryDto toSummaryDto(AccessRequest entity) {
        AppUser requester = Optional.ofNullable(entity.getRequester()).orElse(new AppUser());
        AppUser reviewer = Optional.ofNullable(entity.getReviewer()).orElse(new AppUser());

        return AccessRequestSummaryDto.builder()
                .id(entity.getId())
                .requesterId(requester.getId())
                .requesterName(requester.getFullName())
                .reviewerId(reviewer.getId())
                .reviewerName(reviewer.getFullName())
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
