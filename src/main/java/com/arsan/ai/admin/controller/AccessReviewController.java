package com.arsan.ai.admin.controller;

import com.arsan.ai.admin.model.AccessRequestReviewDto;
import com.arsan.ai.admin.service.AccessReviewService;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.shared.model.AccessRequestSummaryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/access-requests")
@RequiredArgsConstructor
public class AccessReviewController {

    private final AccessReviewService service;

    @GetMapping()
    public Page<AccessRequestSummaryDto> getAccessRequests(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "requestedDate", direction = Sort.Direction.DESC) Pageable pageable) {

        if (status == null) {
            return service.getAll(pageable);
        }
        return service.getByStatus(AccessRequestStatus.valueOf(status), pageable);
    }

    @GetMapping("/{id}")
    public AccessRequestSummaryDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/review")
    public void reviewRequest(@Valid @RequestBody AccessRequestReviewDto reviewDto) {
        service.reviewRequest(reviewDto);
    }
}
