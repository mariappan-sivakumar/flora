package com.mari.flora.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {

    @Size(min = 2, max = 150, message = "Company name must be between 2 and 150 characters")
    private String name;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Size(max = 255, message = "Short description must not exceed 255 characters")
    private String shortDescription;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Pattern(regexp = "^[+]?[0-9]{1,15}$", message = "Phone number must be valid")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    private String emailId;

    @Pattern(regexp = "^[+]?[0-9]{1,15}$", message = "WhatsApp number must be valid")
    private String whatsapp;

    private Long imageId;

    private String heroTitle;

    @Valid
    @Builder.Default
    private List<CompanyKpiRequest> kpis = new ArrayList<>();
}
