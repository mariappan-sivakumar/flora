package com.mari.flora.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegistrationDto {
    Long id;
    String email;
    String name;
    String contact;
    String role;
    String password;
}