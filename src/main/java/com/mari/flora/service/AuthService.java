package com.mari.flora.service;

import com.mari.flora.dto.request.AdminRegistrationDto;
import com.mari.flora.dto.request.LoginRequest;

public interface AuthService {
    String login(LoginRequest loginDto);
    String registerAdmin(AdminRegistrationDto adminRegistrationDto);
}
