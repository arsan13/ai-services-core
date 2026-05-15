package com.arsan.ai.profile.service;

import com.arsan.ai.profile.model.AccessRequestCreateDto;
import com.arsan.ai.profile.model.AccessRequestResponseDto;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccessRequestService {

    AccessRequestResponseDto getById(Long requestId);

    Page<AccessRequestResponseDto> getByStatus(AccessRequestStatus status, Pageable pageable);

    Page<AccessRequestResponseDto> getAll(Pageable pageable);

    AccessRequestResponseDto requestAccess(AccessRequestCreateDto requestDto);

    void cancelRequest(Long requestId);
}
