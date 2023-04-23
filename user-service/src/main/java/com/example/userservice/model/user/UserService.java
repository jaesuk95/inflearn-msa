package com.example.userservice.model.user;

import com.example.userservice.model.user.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);

    // Iterable : 반복적인 데이터
    Iterable<UserEntity> getAllUsers();

    UserDto getUserByUserId(String id);
}
