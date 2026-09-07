package com.mari.flora.repository;

import com.mari.flora.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    @Query(value = """
            SELECT * FROM audit_log al
                     where (:userId is null or al.user_id = :userId)and
                     (:action is null or al.action = :action) and
                     (:entityType is null or al.entity_type = :entityType)
           """, countQuery = """
            SELECT COUNT(*) FROM audit_log al
                     where (:userId is null or al.user_id = :userId) and
                     (:action is null or al.action = :action) and
                     (:entityType is null or al.entity_type = :entityType)
           """, nativeQuery = true)
    Page<AuditLog> findAll(@Param("userId") Long userId,
                           @Param("action") String action,
                           @Param("entityType") String entityType,
                           Pageable pageable);
}
