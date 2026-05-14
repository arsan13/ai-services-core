package com.arsan.ai.admin.service;

import com.arsan.ai.admin.model.AccessRequestReviewDto;
import com.arsan.ai.admin.model.AccessRequestRevokeDto;
import com.arsan.ai.admin.model.AccessRequestSummaryDto;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccessReviewService {

    AccessRequestSummaryDto getById(Long requestId);

    Page<AccessRequestSummaryDto> getByStatus(AccessRequestStatus status, Pageable pageable);

    Page<AccessRequestSummaryDto> getAll(Pageable pageable);

    void reviewRequest(AccessRequestReviewDto reviewDto);

    void revokeRequest(AccessRequestRevokeDto reviewDto);
}
