package com.arsan.ai.accessrequest.model;

import com.arsan.ai.accessrequest.enums.AccessRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAccessRequestCommand {

    private Long requestId;
    private AccessRequestStatus status;
    private String reviewerComment;
}
