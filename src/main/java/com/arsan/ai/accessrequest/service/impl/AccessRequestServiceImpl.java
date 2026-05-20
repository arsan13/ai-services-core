package com.arsan.ai.accessrequest.service.impl;

import com.arsan.ai.accessrequest.entity.AccessRequest;
import com.arsan.ai.accessrequest.events.AccessRequestUpdatedEvent;
import com.arsan.ai.accessrequest.mapper.AccessRequestMapper;
import com.arsan.ai.accessrequest.model.RequestAccessCommand;
import com.arsan.ai.accessrequest.model.ReviewAccessRequestCommand;
import com.arsan.ai.accessrequest.model.RevokeAccessRequestCommand;
import com.arsan.ai.accessrequest.repository.AccessRequestRepository;
import com.arsan.ai.accessrequest.service.AccessRequestService;
import com.arsan.ai.accessrequest.events.AccessRequestApprovedEvent;
import com.arsan.ai.accessrequest.events.AccessRequestRejectedEvent;
import com.arsan.ai.accessrequest.events.AccessRequestRevokedEvent;
import com.arsan.ai.identity.service.PermissionService;
import com.arsan.ai.identity.service.RoleService;
import com.arsan.ai.identity.entity.AppUser;
import com.arsan.ai.shared.util.ExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessRequestServiceImpl implements AccessRequestService {

    private final ApplicationEventPublisher publisher;
    private final AccessRequestRepository accessRequestRepository;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final AccessRequestMapper mapper;

    @Override
    @Transactional
    public AccessRequest requestAccess(RequestAccessCommand requestCommand, AppUser requester) {
        AccessRequest entity = mapper.toEntity(requestCommand);

        entity.setRequester(requester);
        entity.validateCreation();

        AccessRequest saved = accessRequestRepository.save(entity);

        publisher.publishEvent(new AccessRequestUpdatedEvent(entity.getRequester().getId()));
        return saved;
    }

    @Override
    @Transactional
    public void cancelRequest(Long requestId, Long requesterId) {
        AccessRequest request = accessRequestRepository
                .findByIdAndRequesterId(requestId, requesterId)
                .orElseThrow(ExceptionUtils::resourceNotFound);

        request.cancel();
        publisher.publishEvent(new AccessRequestUpdatedEvent(requesterId));
    }

    @Override
    @PreAuthorize("hasAuthority('request:access:approve')")
    @Transactional
    public void reviewRequest(ReviewAccessRequestCommand reviewCommand, AppUser reviewer) {
        AccessRequest request = getRequest(reviewCommand.getRequestId());
        AppUser requester = request.getRequester();

        request.review(reviewCommand.getStatus(), reviewer, reviewCommand.getReviewerComment());

        if (request.isApproved()) {
            roleService.grantRoles(requester.getId(), request.getRoles());
            permissionService.grantPermission(requester.getId(), request.getPermissions());
        }

        publishEvent(request);
    }

    @Override
    @PreAuthorize("hasAuthority('request:access:approve')")
    @Transactional
    public void revokeRequest(RevokeAccessRequestCommand revokeCommand, AppUser reviewer) {
        AccessRequest request = getRequest(revokeCommand.getRequestId());
        AppUser requester = request.getRequester();

        request.revoke(reviewer, revokeCommand.getReviewerComment());

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
        publisher.publishEvent(new AccessRequestUpdatedEvent(request.getRequester().getId()));

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
