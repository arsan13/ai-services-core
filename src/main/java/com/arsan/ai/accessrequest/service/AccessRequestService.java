package com.arsan.ai.accessrequest.service;

import com.arsan.ai.accessrequest.entity.AccessRequest;
import com.arsan.ai.accessrequest.model.RequestAccessCommand;
import com.arsan.ai.accessrequest.model.ReviewAccessRequestCommand;
import com.arsan.ai.accessrequest.model.RevokeAccessRequestCommand;

public interface AccessRequestService {

    AccessRequest requestAccess(RequestAccessCommand requestCommand, Long requesterId);

    void cancelRequest(Long requestId, Long requesterId);

    void reviewRequest(ReviewAccessRequestCommand reviewCommand, Long reviewerId);

    void revokeRequest(RevokeAccessRequestCommand revokeCommand, Long reviewerId);
}
