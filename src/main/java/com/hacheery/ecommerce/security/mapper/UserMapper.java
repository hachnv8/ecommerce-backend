package com.hacheery.ecommerce.security.mapper;

import com.hacheery.ecommerce.security.dto.UserDto;
import com.hacheery.ecommerce.security.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }
}
