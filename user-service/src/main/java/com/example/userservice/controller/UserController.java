package com.example.userservice.controller;

import com.example.userservice.controller.request.RequestUser;
import com.example.userservice.controller.response.ResponseUser;
import com.example.userservice.model.user.UserDto;
import com.example.userservice.model.user.UserEntity;
import com.example.userservice.model.user.UserService;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user-service/")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true) // prometheus 에 등록
    public String welcome() {
        log.info("welcome message sent");
        return message;
    }

    private final UserService userService;
    private final Environment env;

    @Value("${greeting.message}")
    private String message;

    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true) // prometheus 에 등록
    public String status(HttpServletRequest request) {
        return String.format("It's working in User Service"
            + ", port(local.server.port)=" + env.getProperty("local.server.port")
            + ", port(server.port)=" + env.getProperty("server.port")
            + ", with token secret=" + env.getProperty("token.secret")
            + ", with token time=" + env.getProperty("token.expiration_time"));
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

//    @GetMapping("/users/{userId}")
//    public ResponseEntity<ResponseUser> getUserById(@PathVariable("userId") String userId) {
//        UserDto userDto = userService.getUserByUserId(userId);
//        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);
//        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
//    }

    @GetMapping(value = "/users/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
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



}
