package com.example.startup.controller;

import com.example.startup.entity.enums.UserRole;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.auth.Login;
import com.example.startup.payload.auth.Register;
import com.example.startup.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @RequestBody Register register,
            @RequestParam UserRole role) {
        return ResponseEntity.ok(authService.register(register, role));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Login login) {
        return ResponseEntity.ok(authService.login(login));
    }

    @PutMapping("/checkCode")
    public ResponseEntity<ApiResponse> checkCode(@RequestParam Integer code) {
        return ResponseEntity.ok(authService.checkCode(code));
    }
}
