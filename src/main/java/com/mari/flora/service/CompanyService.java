package com.mari.flora.service;

import com.mari.flora.dto.request.CompanyRequest;
import com.mari.flora.dto.response.CompanyResponse;

public interface CompanyService {
    CompanyResponse getActiveCompany();
    String createOrUpdateCompany(CompanyRequest companyRequest);
    String clearCompany();
}
