package com.mari.flora.controller;

import com.mari.flora.dto.request.CompanyRequest;
import com.mari.flora.dto.response.CompanyResponse;
import com.mari.flora.dto.response.ResponseDto;
import com.mari.flora.service.CompanyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@Tag(name = "Company", description = "APIs to manage company information")
@Slf4j
public class CompanyController {
    private final CompanyService companyService;


    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "Get active company", description = "Retrieve the currently active company details")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Company returned"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping
    public ResponseEntity<ResponseDto<CompanyResponse>> getActiveCompany() {
        log.info("getActiveCompany called");
        CompanyResponse result = companyService.getActiveCompany();
        log.debug("getActiveCompany result={}", result);
        return ResponseEntity.ok(ResponseDto.success(result, "Company returned"));

    }

    @Operation(summary = "Create or update company", description = "Create or update company info (admin only)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Company created/updated"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDto<String>> createOrUpdateCompany(@RequestBody CompanyRequest companyRequest) {
        log.info("createOrUpdateCompany called request={}", companyRequest);
        String result = companyService.createOrUpdateCompany(companyRequest);
        log.debug("createOrUpdateCompany result={}", result);
        return ResponseEntity.ok(ResponseDto.success(result, "Company created/updated"));

    }

    @Operation(summary = "Delete company", description = "Delete current company (admin only)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Company deleted"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDto<String>> deleteCompany() {
        log.info("deleteCompany called");
        companyService.clearCompany();
        log.debug("deleteCompany completed");
        return ResponseEntity.ok(ResponseDto.success("Company deleted successfully", "Company deleted"));    }

    @Operation(summary = "Get dashboard overview", description = "Retrieve the dashboard overview information")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Dashboard overview returned"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/dashboard")
    public ResponseEntity<ResponseDto<Object>> getDashboardOverview() {
        log.info("getDashboardOverview called");
        Object result = companyService.getDashboardOverview();
        log.debug("getDashboardOverview result={}", result);
        return ResponseEntity.ok(ResponseDto.success(result, "Dashboard overview returned"));
    }
}
