package com.arsan.ai.profile.service.impl;

import com.arsan.ai.auth.enums.RoleType;
import com.arsan.ai.profile.model.AccessRequestCreateDto;
import com.arsan.ai.profile.model.AccessRequestResponseDto;
import com.arsan.ai.profile.model.PendingRolesPermissionsDto;
import com.arsan.ai.profile.service.AccessRequestService;
import com.arsan.ai.shared.entity.AccessRequest;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.shared.mapper.AccessRequestMapper;
import com.arsan.ai.shared.repository.AccessRequestRepository;
import com.arsan.ai.shared.repository.projection.PendingAccessRequestProjection;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccessRequestServiceImpl implements AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final AccessRequestMapper mapper;

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
        List<PendingAccessRequestProjection> projections = accessRequestRepository
                .findByStatusAndRequesterId(AccessRequestStatus.PENDING, userId, PendingAccessRequestProjection.class);

        Set<RoleType> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        for (PendingAccessRequestProjection projection : projections) {
            roles.addAll(projection.getRoles());
            permissions.addAll(projection.getPermissions());
        }

        return PendingRolesPermissionsDto.builder()
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Override
    public AccessRequestResponseDto requestAccess(AccessRequestCreateDto requestDto) {
        AccessRequest entity = mapper.toEntity(requestDto);

        entity.setRequester(SecurityUtils.getCurrentUserOrThrow());
        entity.validateCreation();

        return mapper.toResponseDto(accessRequestRepository.save(entity));
    }

    @Override
    public void cancelRequest(Long requestId) {
        Long userId = SecurityUtils.getCurrentUserIdOrThrow();

        AccessRequest request = accessRequestRepository
                .findByIdAndRequesterId(requestId, userId)
                .orElseThrow(ExceptionUtils::resourceNotFound);

        request.cancel();
        accessRequestRepository.save(request);
    }
}
