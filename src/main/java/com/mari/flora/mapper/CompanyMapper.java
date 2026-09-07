package com.mari.flora.mapper;

import com.mari.flora.dto.request.CompanyKpiRequest;
import com.mari.flora.dto.request.CompanyRequest;
import com.mari.flora.dto.response.CompanyKpiResponse;
import com.mari.flora.dto.response.CompanyResponse;
import com.mari.flora.entity.Company;
import com.mari.flora.entity.CompanyKpi;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    public CompanyResponse toResponse(Company company) {
        if (company == null) {
            return null;
        }

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .shortDescription(company.getShortDescription())
                .imageId(company.getImage() != null ? company.getImage().getImageId() : null)
                .address(company.getAddress())
                .phoneNumber(company.getPhoneNumber())
                .emailId(company.getEmailId())
                .whatsapp(company.getWhatsapp())
                .isActive(company.getIsActive())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .heroTitle(company.getHeroTitle())
                .imageUrl(company.getImage() != null ? company.getImage().getImagePath() : null)
                // ---- ADDED: active KPIs only, ordered by orderBy ----
                .kpis(mapKpisToResponse(company.getKpis()))
                .build();
    }

    public Company toEntity(CompanyResponse response) {
        if (response == null) {
            return null;
        }

        return Company.builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .shortDescription(response.getShortDescription())
                .address(response.getAddress())
                .phoneNumber(response.getPhoneNumber())
                .emailId(response.getEmailId())
                .whatsapp(response.getWhatsapp())
                .isActive(response.getIsActive())
                .heroTitle(response.getHeroTitle())
                .build();
    }

    public Company toEntity(CompanyRequest companyRequest) {
        if (companyRequest == null) {
            return null;
        }

        return Company.builder()
                .name(companyRequest.getName())
                .description(companyRequest.getDescription())
                .shortDescription(companyRequest.getShortDescription())
                .address(companyRequest.getAddress())
                .phoneNumber(companyRequest.getPhoneNumber())
                .emailId(companyRequest.getEmailId())
                .whatsapp(companyRequest.getWhatsapp())
                .heroTitle(companyRequest.getHeroTitle())
                .build();
        // NOTE: kpis intentionally NOT built here — CompanyKpi requires a
        // managed/being-persisted `company` reference. Call toKpiEntities()
        // separately in the service after the Company instance exists.
    }

    // ---- ADDED: builds CompanyKpi entities linked back to the given company ----
    public List<CompanyKpi> toKpiEntities(Company company, List<CompanyKpiRequest> kpiRequests) {
        if (kpiRequests == null) {
            return List.of();
        }
        return kpiRequests.stream()
                .map(req -> CompanyKpi.builder()
                        .company(company)
                        .kpiValue(req.getKpiValue())
                        .kpiLabel(req.getKpiLabel())
                        .orderBy(req.getOrderBy())
                        .isActive(true)
                        .build())
                .collect(Collectors.toList());
    }

    // ---- ADDED: private helper for toResponse() ----
    private List<CompanyKpiResponse> mapKpisToResponse(List<CompanyKpi> kpis) {
        if (kpis == null) {
            return List.of();
        }
        return kpis.stream()
                .filter(CompanyKpi::getIsActive)
                .sorted(Comparator.comparing(CompanyKpi::getOrderBy))
                .map(k -> CompanyKpiResponse.builder()
                        .kpiValue(k.getKpiValue())
                        .kpiLabel(k.getKpiLabel())
                        .build())
                .collect(Collectors.toList());
    }
}