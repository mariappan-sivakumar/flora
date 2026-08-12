package com.mari.flora.service.impl;

import com.mari.flora.audit.Auditable;
import com.mari.flora.dto.enums.AuditAction;
import com.mari.flora.dto.enums.AuditEntityType;
import com.mari.flora.dto.request.CompanyRequest;
import com.mari.flora.dto.response.CompanyResponse;
import com.mari.flora.entity.Company;
import com.mari.flora.mapper.CompanyMapper;
import com.mari.flora.repository.CompanyRepository;
import com.mari.flora.service.CompanyService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService{
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public CompanyResponse getActiveCompany() {
        log.info("getActiveCompany called");
        CompanyResponse response = companyMapper.toResponse(companyRepository.findCompanyByIsActiveTrue());
        log.debug("getActiveCompany result={}", response);
        return response;
    }

    @Override
    @Transactional
    @Auditable(
            action = AuditAction.CREATE,
            entityType = AuditEntityType.COMPANY,
            message = "Company '#{#companyRequest.name}' created/updated by #{#username}"
    )
    public String createOrUpdateCompany(CompanyRequest companyRequest) {
        log.info("createOrUpdateCompany called name='{}'", companyRequest.getName());
        Company company = companyMapper.toEntity(companyRequest);
        companyRepository.softDeleteAllCompany();
        company.setIsActive(true);
        company.getKpis().clear();
        company.getKpis().addAll(companyMapper.toKpiEntities(company, companyRequest.getKpis()));
        companyRepository.save(company);
        log.debug("createOrUpdateCompany completed for name={}", companyRequest.getName());
        return "Company created or updated successfully";
    }

    @Override
    @Transactional
    @Auditable(
            action = AuditAction.DELETE,
            entityType = AuditEntityType.COMPANY,
            message = "Active company record cleared by #{#username}"
    )
    public String clearCompany() {
        log.info("clearCompany called");
        companyRepository.softDeleteAllCompany();
        log.debug("clearCompany completed");
        return "Company cleared successfully";
    }
}
