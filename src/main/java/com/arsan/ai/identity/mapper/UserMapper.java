package com.arsan.ai.identity.mapper;

import com.arsan.ai.identity.entity.AppUser;
import com.arsan.ai.identity.model.AppUserDto;
import com.arsan.ai.identity.model.UserEvictDto;
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

    public AppUserDto toDto(AppUser entity) {
        AppUserDto dto = modelMapper.map(entity, AppUserDto.class);
        dto.setRoles(Set.copyOf(entity.getRoles()));
        dto.setPermissions(PermissionUtils.resolvePermissions(entity));
        return dto;
    }

    public UserProfile toUserProfile(AppUserDto dto) {
        UserProfile userProfile = modelMapper.map(dto, UserProfile.class);
        userProfile.setRoles(Set.copyOf(dto.getRoles()));
        userProfile.setPermissions(Set.copyOf(dto.getPermissions()));
        userProfile.setHasPassword(dto.getPassword() != null);
        return userProfile;
    }

    public UserEvictDto toEvictDto(AppUser entity) {
        return new UserEvictDto(entity.getId(), entity.getEmail());
    }
}
