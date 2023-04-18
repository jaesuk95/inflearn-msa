package com.example.userservice.controller.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterPayload {
    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email cannot be less than two characters")
    @Email
    private String email;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equal or greater than 8 characters and less than 16 characters")
    private String pwd;
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name not be less than two characters")
    private String name;
}
