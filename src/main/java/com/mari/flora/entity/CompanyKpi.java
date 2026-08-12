package com.mari.flora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_kpi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyKpi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "fk_company_kpi_company_id"))
    private Company company;

    // e.g. "20+", "100%", "Daily", "2 Gen" — kept as String since not all KPIs are numeric
    @Column(name = "kpi_value", nullable = false, length = 30)
    private String kpiValue;

    // e.g. "Countries We Export To", "Own-Farm Sourced Blooms"
    @Column(name = "kpi_label", nullable = false, length = 150)
    private String kpiLabel;

    @Column(name = "order_by", nullable = false)
    @Builder.Default
    private Integer orderBy = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}