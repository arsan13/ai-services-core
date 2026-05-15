package com.arsan.ai.admin.model;

import com.arsan.ai.shared.enums.AccessRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestReviewDto {

    @NotNull
    private Long requestId;

    @NotNull
    private AccessRequestStatus status;

    private String reviewerComment;
}
