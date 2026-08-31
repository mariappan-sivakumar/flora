package com.mari.flora.repository;

import com.mari.flora.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findBy(Pageable pageable);
}
