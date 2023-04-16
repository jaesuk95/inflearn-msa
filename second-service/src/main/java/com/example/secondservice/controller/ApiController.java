package com.example.secondservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first-service")
public class ApiController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to 2nd service";
    }
}
