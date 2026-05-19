package com.arsan.ai.admin.service.impl;

import com.arsan.ai.accessrequest.enums.AccessRequestStatus;
import com.arsan.ai.accessrequest.repository.AccessRequestRepository;
import com.arsan.ai.accessrequest.service.AccessRequestService;
import com.arsan.ai.admin.mapper.AccessReviewMapper;
import com.arsan.ai.admin.model.AccessRequestReviewDto;
import com.arsan.ai.admin.model.AccessRequestRevokeDto;
import com.arsan.ai.admin.model.AccessRequestSummaryDto;
import com.arsan.ai.admin.service.AccessReviewService;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessReviewServiceImpl implements AccessReviewService {

    private final AccessRequestRepository accessRequestRepository;
    private final AccessRequestService accessRequestService;
    private final AccessReviewMapper mapper;

    @Override
    public AccessRequestSummaryDto getById(Long requestId) {
        return accessRequestRepository
                .findById(requestId)
                .map(mapper::toSummaryDto)
                .orElseThrow(ExceptionUtils::resourceNotFound);
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
    public void reviewRequest(AccessRequestReviewDto reviewDto) {
        accessRequestService.reviewRequest(mapper.toReviewCommand(reviewDto), SecurityUtils.getCurrentUserOrThrow());
    }

    @Override
    public void revokeRequest(AccessRequestRevokeDto revokeDto) {
        accessRequestService.revokeRequest(mapper.toRevokeCommand(revokeDto), SecurityUtils.getCurrentUserOrThrow());
    }
}
