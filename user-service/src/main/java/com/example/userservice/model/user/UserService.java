package com.example.userservice.model.user;

import com.example.userservice.model.user.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

// UserDetailsService, spring security 에 있는 클래스를 상속받는다
public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    // Iterable : 반복적인 데이터
    Iterable<UserEntity> getAllUsers();

    UserDto getUserByUserId(String id);

    UserDto getUserDetailsByEmail(String username);
}
