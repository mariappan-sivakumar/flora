package com.mari.flora.mapper;

import com.mari.flora.dto.response.UserResponse;
import com.mari.flora.entity.Role;
import com.mari.flora.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .roles(user.getRoles() != null ?
                        user.getRoles().stream()
                                .map(Role::getRoleName)
                                .collect(Collectors.toSet()) :
                        null)
                .build();
    }
}
