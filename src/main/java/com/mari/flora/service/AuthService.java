package com.mari.flora.service;

import com.mari.flora.dto.request.AdminRegistrationDto;
import com.mari.flora.dto.request.LoginRequest;
import com.mari.flora.dto.response.UserResponse;

import java.util.List;

public interface AuthService {
    String login(LoginRequest loginDto);
    String registerAdmin(AdminRegistrationDto adminRegistrationDto);

    List<UserResponse> listUsers(String role);
}
