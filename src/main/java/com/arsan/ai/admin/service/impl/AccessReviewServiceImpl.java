package com.arsan.ai.admin.service.impl;

import com.arsan.ai.admin.events.AccessRequestApprovedEvent;
import com.arsan.ai.admin.events.AccessRequestRejectedEvent;
import com.arsan.ai.admin.model.AccessRequestReviewDto;
import com.arsan.ai.admin.service.AccessReviewService;
import com.arsan.ai.shared.entity.AccessRequest;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.shared.mapper.AccessRequestMapper;
import com.arsan.ai.shared.model.AccessRequestSummaryDto;
import com.arsan.ai.shared.repository.AccessRequestRepository;
import com.arsan.ai.shared.util.ExceptionUtils;
import com.arsan.ai.shared.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccessReviewServiceImpl implements AccessReviewService {

    private final ApplicationEventPublisher publisher;
    private final AccessRequestRepository accessRequestRepository;
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
        AppUser reviewer = SecurityUtils.getCurrentUserOrThrow();
        AccessRequest request = accessRequestRepository
                .findById(reviewDto.getRequestId())
                .orElseThrow(ExceptionUtils::resourceNotFound);

        if (request.getStatus() == AccessRequestStatus.APPROVED) {
            throw new IllegalStateException("Request already reviewed");
        }
        if (request.getStatus() == AccessRequestStatus.CANCELLED) {
            throw new IllegalStateException("Request already cancelled");
        }
        if (request.getStatus() == AccessRequestStatus.REJECTED) {
            throw new IllegalStateException("Request already rejected");
        }

        request.setStatus(reviewDto.getStatus());
        request.setReviewer(reviewer);
        request.setReviewerComment(reviewDto.getReviewerComment());
        request.setRequestedDate(LocalDateTime.now());

        publishEvent(request);
    }

    private void publishEvent(AccessRequest request) {
        if (request.getStatus() == AccessRequestStatus.APPROVED) {
            publisher.publishEvent(new AccessRequestApprovedEvent(
                    request.getId(), request.getRequester().getEmail(), request.getRequester().getFullName()));
        } else {
            publisher.publishEvent(new AccessRequestRejectedEvent(
                    request.getId(), request.getRequester().getEmail(), request.getRequester().getFullName(), request.getReviewerComment()));
        }
    }
}
