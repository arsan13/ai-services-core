package com.arsan.ai.shared.mapper;

import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.profile.model.UserProfile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserProfile toUserProfile(AppUser user) {
        UserProfile userProfile = modelMapper.map(user, UserProfile.class);
        userProfile.setRoles(Set.copyOf(user.getRoles()));
        userProfile.setPermissions(Set.copyOf(user.getPermissions()));
        return userProfile;
    }
}
