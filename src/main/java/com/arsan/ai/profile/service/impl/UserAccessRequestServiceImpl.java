package com.arsan.ai.profile.service.impl;

import com.arsan.ai.accessrequest.cache.AccessRequestCache;
import com.arsan.ai.accessrequest.entity.AccessRequest;
import com.arsan.ai.accessrequest.enums.AccessRequestStatus;
import com.arsan.ai.accessrequest.repository.AccessRequestRepository;
import com.arsan.ai.accessrequest.service.AccessRequestService;
import com.arsan.ai.profile.mapper.UserAccessRequestMapper;
import com.arsan.ai.profile.model.AccessRequestCreateDto;
import com.arsan.ai.profile.model.AccessRequestResponseDto;
import com.arsan.ai.profile.service.UserAccessRequestService;
import com.arsan.ai.shared.model.PendingRolesPermissionsDto;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccessRequestServiceImpl implements UserAccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final AccessRequestService requestService;
    private final UserAccessRequestMapper mapper;
    private final AccessRequestCache requestCache;

    @Override
    public AccessRequestResponseDto getById(Long requestId) {
        Long userId = SecurityUtils.getCurrentUserIdOrThrow();
        return accessRequestRepository
                .findByIdAndRequesterId(requestId, userId)
                .map(mapper::toResponseDto)
                .orElseThrow(ExceptionUtils::resourceNotFound);
    }

    @Override
    public Page<AccessRequestResponseDto> getByStatus(AccessRequestStatus status, Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserIdOrThrow();
        return accessRequestRepository
                .findByStatusAndRequesterId(status, userId, pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    public Page<AccessRequestResponseDto> getAll(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserIdOrThrow();
        return accessRequestRepository
                .findByRequesterId(userId, pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    public PendingRolesPermissionsDto getPendingRolesAndPermissions() {
        Long userId = SecurityUtils.getCurrentUserIdOrThrow();
        return requestCache.getPendingByUser(userId);
    }

    @Override
    public AccessRequestResponseDto requestAccess(AccessRequestCreateDto requestDto) {
        final AccessRequest accessRequest = requestService.requestAccess(mapper.toRequestCommand(requestDto), SecurityUtils.getCurrentUserIdOrThrow());
        return mapper.toResponseDto(accessRequest);
    }

    @Override
    public void cancelRequest(Long requestId) {
        requestService.cancelRequest(requestId, SecurityUtils.getCurrentUserIdOrThrow());
    }
}
