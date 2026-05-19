package com.arsan.ai.accessrequest.service;

import com.arsan.ai.accessrequest.entity.AccessRequest;
import com.arsan.ai.accessrequest.model.RequestAccessCommand;
import com.arsan.ai.accessrequest.model.ReviewAccessRequestCommand;
import com.arsan.ai.accessrequest.model.RevokeAccessRequestCommand;
import com.arsan.ai.identity.entity.AppUser;

public interface AccessRequestService {

    AccessRequest requestAccess(RequestAccessCommand requestCommand, AppUser requester);

    void cancelRequest(Long requestId, Long requesterId);

    void reviewRequest(ReviewAccessRequestCommand reviewCommand, AppUser reviewer);

    void revokeRequest(RevokeAccessRequestCommand revokeCommand, AppUser reviewer);
}
