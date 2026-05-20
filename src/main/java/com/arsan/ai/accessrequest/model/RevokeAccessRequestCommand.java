package com.arsan.ai.accessrequest.model;

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
public class RevokeAccessRequestCommand {

    private Long requestId;
    private String reviewerComment;
}
