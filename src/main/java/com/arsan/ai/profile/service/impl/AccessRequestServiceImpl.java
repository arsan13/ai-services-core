package com.arsan.ai.profile.service.impl;

import com.arsan.ai.profile.model.AccessRequestCreateDto;
import com.arsan.ai.profile.service.AccessRequestService;
import com.arsan.ai.shared.entity.AccessRequest;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.shared.mapper.AccessRequestMapper;
import com.arsan.ai.shared.model.AccessRequestResponseDto;
import com.arsan.ai.shared.repository.AccessRequestRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
