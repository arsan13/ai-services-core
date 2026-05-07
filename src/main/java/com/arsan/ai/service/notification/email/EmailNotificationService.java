package com.arsan.ai.service.notification.email;

import com.arsan.ai.entity.AppUser;

public interface EmailNotificationService {

    void sendEmailVerificationEmail(AppUser user);

    void sendPasswordResetEmail(AppUser user);
}
