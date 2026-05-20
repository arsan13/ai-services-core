package com.arsan.ai.identity.mapper;

import com.arsan.ai.identity.entity.AppUser;
import com.arsan.ai.identity.util.PermissionUtils;
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
        userProfile.setPermissions(PermissionUtils.resolvePermissions(user));
        userProfile.setHasPassword(user.getPassword() != null);
        return userProfile;
    }
}
