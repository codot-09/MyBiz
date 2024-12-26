package com.example.startup.payload.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record Register (
        @NotBlank
        @Email
        String email,
        @NotBlank
        String fullname,
        @NotBlank
        String password
){
}
