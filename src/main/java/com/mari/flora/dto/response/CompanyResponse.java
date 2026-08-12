package com.mari.flora.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponse {

    private Integer id;
    private String name;
    private String description;
    private String shortDescription;
    private Integer imageId;
    private String address;
    private String phoneNumber;
    private String emailId;
    private String whatsapp;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String heroTitle;
    private List<CompanyKpiResponse> kpis;
}
