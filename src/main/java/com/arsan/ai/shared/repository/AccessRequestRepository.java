package com.arsan.ai.shared.repository;

import com.arsan.ai.shared.entity.AccessRequest;
import com.arsan.ai.shared.enums.AccessRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {

    @EntityGraph(attributePaths = {"reviewer"})
    Optional<AccessRequest> findByIdAndRequesterId(Long id, Long requesterId);

    @EntityGraph(attributePaths = {"reviewer"})
    Page<AccessRequest> findByStatusAndRequesterId(AccessRequestStatus status, Long requesterId, Pageable pageable);

    @EntityGraph(attributePaths = {"reviewer"})
    Page<AccessRequest> findByRequesterId(Long requesterId, Pageable pageable);

    @EntityGraph(attributePaths = {"requester", "reviewer"})
    Page<AccessRequest> findByStatus(AccessRequestStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"requester", "reviewer"})
    Page<AccessRequest> findAll(Pageable pageable);

    boolean existsByStatusAndRequesterId(AccessRequestStatus accessRequestStatus, Long requesterId);
}
