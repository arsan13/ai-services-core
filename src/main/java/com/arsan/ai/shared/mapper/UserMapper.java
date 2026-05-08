package com.arsan.ai.shared.mapper;

import com.arsan.ai.profile.model.UserProfile;
import com.arsan.ai.shared.entity.AppUser;
import com.arsan.ai.shared.util.PermissionUtils;
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
