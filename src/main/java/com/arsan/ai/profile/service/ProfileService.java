package com.arsan.ai.profile.service;

import com.arsan.ai.profile.model.ChangePasswordRequest;
import com.arsan.ai.profile.model.UserProfile;

public interface ProfileService {

    UserProfile getUserProfile();

    void changePassword(ChangePasswordRequest request);

    void logoutOfAllDevices();
}
