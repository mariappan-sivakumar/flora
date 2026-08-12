package com.mari.flora.controller;

import com.mari.flora.dto.request.AdminRegistrationDto;
import com.mari.flora.dto.request.LoginRequest;
import com.mari.flora.dto.response.JWTAuthResponse;
import com.mari.flora.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "APIs for Authentication like login, register, etc.")
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //    Build Login Rest Api
    @Operation(summary = "Login", description = "Login with email and password")
    @ApiResponse(responseCode = "200", description = "Login successfully")
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginRequest loginDto) {
        log.info("login called for username/email (masked)");
        String token = authService.login(loginDto);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setToken(token);
        log.debug("login success, token length={}", token != null ? token.length() : 0);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "Register Admin", description = "Register User For admin")
    @ApiResponse(responseCode = "200", description = "Admin registered successfully")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegistrationDto adminRegistrationDto){
        log.info("registerAdmin called for email (masked)");
        String result = authService.registerAdmin(adminRegistrationDto);
        log.debug("registerAdmin result={}", result);
        return ResponseEntity.ok(result);
    }
}
