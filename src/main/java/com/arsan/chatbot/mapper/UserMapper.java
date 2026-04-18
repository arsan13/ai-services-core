package com.arsan.chatbot.mapper;

import com.arsan.chatbot.entity.User;
import com.arsan.chatbot.model.user.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    List<UserDTO> toDtoList(List<User> users);
}
