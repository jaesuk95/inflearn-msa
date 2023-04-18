package com.example.userservice.controller;

import com.example.userservice.controller.payload.UserRegisterPayload;
import com.example.userservice.model.user.UserDto;
import com.example.userservice.model.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${greeting.message}")
    private String message;

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return "It's working in User Service under Port:" + request.getServerPort();
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {
        return message;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(HttpServletRequest request, @RequestBody UserRegisterPayload userPayload) {
        log.info("service is running in port ={}",request.getServerPort());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userPayload, UserDto.class);
        userService.createUser(userDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
