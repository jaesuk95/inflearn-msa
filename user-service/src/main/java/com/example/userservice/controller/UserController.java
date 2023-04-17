package com.example.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class UserController {

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
}
