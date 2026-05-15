package com.arsan.ai.admin.service.impl;

import com.arsan.ai.admin.events.AccessRequestApprovedEvent;
import com.arsan.ai.admin.events.AccessRequestRejectedEvent;
import com.arsan.ai.admin.events.AccessRequestRevokedEvent;
import com.arsan.ai.admin.model.AccessRequestReviewDto;
import com.arsan.ai.admin.model.AccessRequestRevokeDto;
import com.arsan.ai.admin.model.AccessRequestSummaryDto;
import com.arsan.ai.admin.service.AccessReviewService;
import com.arsan.ai.admin.service.PermissionService;
import com.arsan.ai.admin.service.RoleService;
import com.arsan.ai.shared.entity.AccessRequest;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.shared.mapper.AccessRequestMapper;
import com.arsan.ai.shared.repository.AccessRequestRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessReviewServiceImpl implements AccessReviewService {

    private final ApplicationEventPublisher publisher;
    private final AccessRequestRepository accessRequestRepository;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final AccessRequestMapper mapper;

    @Override
    public AccessRequestSummaryDto getById(Long requestId) {
        return accessRequestRepository
                .findById(requestId)
                .map(mapper::toSummaryDto)
                .orElseThrow(() -> new RuntimeException("Access request not found"));
    }

    @Override
    public Page<AccessRequestSummaryDto> getByStatus(AccessRequestStatus status, Pageable pageable) {
        return accessRequestRepository
                .findByStatus(status, pageable)
                .map(mapper::toSummaryDto);
    }

    @Override
    public Page<AccessRequestSummaryDto> getAll(Pageable pageable) {
        return accessRequestRepository
                .findAll(pageable)
                .map(mapper::toSummaryDto);
    }

    @Override
    @Transactional
    public void reviewRequest(AccessRequestReviewDto reviewDto) {
        AccessRequest request = getRequest(reviewDto.getRequestId());
        AppUser reviewer = SecurityUtils.getCurrentUserOrThrow();
        AppUser requester = request.getRequester();

        request.review(reviewDto.getStatus(), reviewer, reviewDto.getReviewerComment());

        if (request.isApproved()) {
            roleService.grantRoles(requester.getId(), request.getRoles());
            permissionService.grantPermission(requester.getId(), request.getPermissions());
        }

        publishEvent(request);
    }

    @Override
    @Transactional
    public void revokeRequest(AccessRequestRevokeDto reviewDto) {
        AccessRequest request = getRequest(reviewDto.getRequestId());
        AppUser reviewer = SecurityUtils.getCurrentUserOrThrow();
        AppUser requester = request.getRequester();

        request.revoke(reviewer, reviewDto.getReviewerComment());

        roleService.revokeRoles(requester.getId(), request.getRoles());
        permissionService.revokePermission(requester.getId(), request.getPermissions());

        publishEvent(request);
    }

    private AccessRequest getRequest(Long requestId) {
        return accessRequestRepository
                .findById(requestId)
                .orElseThrow(ExceptionUtils::resourceNotFound);
    }

    private void publishEvent(AccessRequest request) {
        switch (request.getStatus()) {
            case APPROVED -> publisher.publishEvent(
                    new AccessRequestApprovedEvent(
                            request.getId(),
                            request.getRequester().getEmail(),
                            request.getRequester().getFullName()
                    )
            );
            case REJECTED -> publisher.publishEvent(
                    new AccessRequestRejectedEvent(
                            request.getId(),
                            request.getRequester().getEmail(),
                            request.getRequester().getFullName(),
                            request.getReviewerComment()
                    )
            );
            case REVOKED -> publisher.publishEvent(
                    new AccessRequestRevokedEvent(
                            request.getId(),
                            request.getRequester().getEmail(),
                            request.getRequester().getFullName(),
                            request.getReviewerComment()
                    )
            );
            default -> throw new IllegalStateException(
                    "Unknown request status: " + request.getStatus()
            );
        }
    }
}
