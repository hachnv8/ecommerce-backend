package com.hacheery.ecommerce.security.service;

import com.hacheery.ecommerce.security.dto.UserDto;
import com.hacheery.ecommerce.security.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getUsers();

    User getUserById(Long userId);

    User updateUser(Long userId, User user);
    UserDto findByUsername(String username);

}
