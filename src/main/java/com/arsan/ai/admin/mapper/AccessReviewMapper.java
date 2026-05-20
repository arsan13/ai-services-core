package com.arsan.ai.admin.mapper;

import com.arsan.ai.accessrequest.entity.AccessRequest;
import com.arsan.ai.accessrequest.model.ReviewAccessRequestCommand;
import com.arsan.ai.accessrequest.model.RevokeAccessRequestCommand;
import com.arsan.ai.admin.model.AccessRequestReviewDto;
import com.arsan.ai.admin.model.AccessRequestRevokeDto;
import com.arsan.ai.admin.model.AccessRequestSummaryDto;
import com.arsan.ai.identity.entity.AppUser;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AccessReviewMapper {

    public ReviewAccessRequestCommand toReviewCommand(AccessRequestReviewDto reviewDto) {
        return ReviewAccessRequestCommand.builder()
                .requestId(reviewDto.getRequestId())
                .status(reviewDto.getStatus())
                .reviewerComment(reviewDto.getReviewerComment())
                .build();
    }

    public RevokeAccessRequestCommand toRevokeCommand(AccessRequestRevokeDto revokeDto) {
        return RevokeAccessRequestCommand.builder()
                .requestId(revokeDto.getRequestId())
                .reviewerComment(revokeDto.getReviewerComment())
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
