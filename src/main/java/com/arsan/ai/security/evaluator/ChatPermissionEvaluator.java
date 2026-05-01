package com.arsan.ai.security.evaluator;

import com.arsan.ai.enums.ChatType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class ChatPermissionEvaluator {

    public boolean hasAccess(Authentication authentication, String chatType) {
        ChatType type = ChatType.fromCode(chatType);
        return hasAccess(authentication, type);
    }

    public boolean hasAccess(Authentication authentication, ChatType chatType) {
        if (authentication == null || chatType == null) {
            return false;
        }

        String requiredAuthority = chatType.getPermission();

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(requiredAuthority::equals);
    }
}
