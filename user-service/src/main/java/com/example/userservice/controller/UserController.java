package com.example.userservice.controller;

import com.example.userservice.controller.request.RequestLogin;
import com.example.userservice.controller.request.RequestUser;
import com.example.userservice.controller.response.ResponseUser;
import com.example.userservice.model.user.UserDto;
import com.example.userservice.model.user.UserEntity;
import com.example.userservice.model.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Environment environment;

    @Value("${greeting.message}")
    private String message;

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
//        return "It's working in User Service under Port:" + request.getServerPort();
        return String.format("User Service on PORT : %s", environment.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {
        return message;
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(HttpServletRequest request, @RequestBody RequestUser userPayload) {
        log.info("service is running in port ={}",request.getServerPort());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userPayload, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUserById(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(HttpServletRequest request) {
        Iterable<UserEntity> userList = userService.getAllUsers();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @PostMapping("/login")
//    public ResponseEntity<ResponseUser> login(
//            @RequestBody RequestLogin requestLogin,
//            HttpServletRequest request) {
//
//        userService.loadUserByUsername()
//        return ResponseEntity.status(HttpStatus.OK);
//    }

}
