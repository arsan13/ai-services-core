package com.arsan.ai.profile.controller;

import com.arsan.ai.profile.model.AccessRequestCreateDto;
import com.arsan.ai.profile.service.AccessRequestService;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import com.arsan.ai.profile.model.AccessRequestResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/access-requests")
@RequiredArgsConstructor
public class AccessRequestController {

    private final AccessRequestService service;

    @GetMapping("/{requestId}")
    public AccessRequestResponseDto getAccessRequestById(@PathVariable Long requestId) {
        return service.getById(requestId);
    }

    @GetMapping
    public Page<AccessRequestResponseDto> getAllAccessRequests(
            @PageableDefault(sort = "requestedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/status/{status}")
    public Page<AccessRequestResponseDto> getAccessRequestsByStatus(
            @PathVariable AccessRequestStatus status,
            @PageableDefault(sort = "requestedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.getByStatus(status, pageable);
    }

    @PostMapping()
    public AccessRequestResponseDto requestAccess(@RequestBody @Valid AccessRequestCreateDto requestDto) {
        return service.requestAccess(requestDto);
    }

    @PutMapping("/{requestId}/cancel")
    public void cancelAccessRequest(@PathVariable Long requestId) {
        service.cancelRequest(requestId);
    }
}
