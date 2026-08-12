package com.mari.flora.repository;

import com.mari.flora.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findCompanyByIsActiveTrue();

    @Modifying
    @Transactional
    @Query(value = "UPDATE company SET is_active = false WHERE is_active = true", nativeQuery = true)
    int softDeleteAllCompany();
}
