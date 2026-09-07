package com.mari.flora.service.impl;

import com.mari.flora.dto.request.AdminRegistrationDto;
import com.mari.flora.dto.request.LoginRequest;
import com.mari.flora.dto.response.UserResponse;
import com.mari.flora.entity.Role;
import com.mari.flora.entity.User;
import com.mari.flora.exception.FunctionalException;
import com.mari.flora.exception.ResourceNotFoundException;
import com.mari.flora.repository.RoleRepository;
import com.mari.flora.repository.UserRepository;
import com.mari.flora.security.JwtTokenProvider;
import com.mari.flora.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(LoginRequest loginDto) {
        log.info("login attempt for username='{}'", loginDto.getUsername());
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        log.debug("login successful, tokenLength={}", token != null ? token.length() : 0);
        return token;
    }

    @Override
    public String registerAdmin(AdminRegistrationDto adminRegistrationDto) {
        log.info("registerAdmin called for email='{}'", adminRegistrationDto.getEmail());
        if (userRepository.existsByEmail(adminRegistrationDto.getEmail())){
            log.warn("registerAdmin - email already exists: {}", adminRegistrationDto.getEmail());
            throw new FunctionalException(HttpStatus.BAD_REQUEST,"Email already exists");
        }
        User user=new User();
        user.setEmail(adminRegistrationDto.getEmail());
        user.setUsername(adminRegistrationDto.getName());
        if (adminRegistrationDto.getRole().equals("admin")) {
            Role roles = (roleRepository.findByRoleName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role not found")));
            user.setRoles(Set.of(roles));
        }
        user.setPassword(passwordEncoder.encode(adminRegistrationDto.getPassword()));
        User savedUser=userRepository.save(user);
        log.debug("registerAdmin saved userId={}", savedUser.getId());
        return "User Created Successfully"+savedUser.getId();
    }

    @Override
    public List<UserResponse> listUsers(String role) {
        return userRepository.findByRoles_RoleName(role).stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getIsActive(), user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet())))
                .toList();
    }
}
