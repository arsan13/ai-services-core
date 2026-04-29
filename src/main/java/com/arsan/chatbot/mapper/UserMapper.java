package com.arsan.chatbot.mapper;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.model.common.UserProfile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserProfile toUserProfile(User user) {
        UserProfile userProfile = modelMapper.map(user, UserProfile.class);
        userProfile.setRoles(Set.copyOf(user.getRoles()));
        userProfile.setPermissions(Set.copyOf(user.getPermissions()));
        return userProfile;
    }
}
