package com.example.userservice.controller.response;

import lombok.Data;

@Data
public class UserRegisterResponse {
    private String email;
    private String name;
    private String userId;
}
