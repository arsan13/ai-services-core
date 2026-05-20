package com.arsan.ai.profile.service;

import com.arsan.ai.accessrequest.enums.AccessRequestStatus;
import com.arsan.ai.profile.model.AccessRequestCreateDto;
import com.arsan.ai.profile.model.AccessRequestResponseDto;
import com.arsan.ai.shared.model.PendingRolesPermissionsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAccessRequestService {

    AccessRequestResponseDto getById(Long requestId);

    Page<AccessRequestResponseDto> getByStatus(AccessRequestStatus status, Pageable pageable);

    Page<AccessRequestResponseDto> getAll(Pageable pageable);

    PendingRolesPermissionsDto getPendingRolesAndPermissions();

    AccessRequestResponseDto requestAccess(AccessRequestCreateDto requestDto);

    void cancelRequest(Long requestId);
}
