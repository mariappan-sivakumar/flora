package com.mari.flora.repository;

import com.mari.flora.dto.response.DashboardOverviewProjection;
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

    @Query(value = """
        WITH category_stats AS (
            SELECT
                COUNT(*) FILTER (WHERE is_active = true)                                   AS total_collections,
                COUNT(*) FILTER (
                    WHERE is_active = true
                      AND created_at >= date_trunc('month', CURRENT_DATE)
                )                                                                          AS collections_added_this_month
            FROM public.category
        ),
        garland_stats AS (
            SELECT
                COUNT(*) FILTER (WHERE is_active = true)                                   AS total_garlands,
                COUNT(*) FILTER (WHERE is_active = true AND is_available = true)           AS available_garlands,
                COUNT(*) FILTER (WHERE is_active = true AND is_available = false)          AS unavailable_garlands,
                COUNT(DISTINCT category_id) FILTER (WHERE is_active = true)                AS categories_with_garlands
            FROM public.garland
        ),
        company_stats AS (
            SELECT
                ROUND(
                    (
                        (CASE WHEN NULLIF(description, '')       IS NOT NULL THEN 1 ELSE 0 END) +
                        (CASE WHEN NULLIF(short_description, '') IS NOT NULL THEN 1 ELSE 0 END) +
                        (CASE WHEN NULLIF(address, '')           IS NOT NULL THEN 1 ELSE 0 END) +
                        (CASE WHEN NULLIF(phone_number, '')      IS NOT NULL THEN 1 ELSE 0 END) +
                        (CASE WHEN NULLIF(email_id, '')          IS NOT NULL THEN 1 ELSE 0 END) +
                        (CASE WHEN NULLIF(whatsapp, '')          IS NOT NULL THEN 1 ELSE 0 END) +
                        (CASE WHEN image_id                      IS NOT NULL THEN 1 ELSE 0 END) +
                        (CASE WHEN NULLIF(hero_title, '')        IS NOT NULL THEN 1 ELSE 0 END)
                    )::numeric / 8.0 * 100, 0
                ) AS profile_completeness_pct
            FROM public.company
            WHERE is_active = true
            LIMIT 1
        )
        SELECT
            cs.total_collections               AS totalCollections,
            cs.collections_added_this_month     AS collectionsAddedThisMonth,
            gs.total_garlands                   AS totalGarlands,
            gs.categories_with_garlands          AS categoriesWithGarlands,
            gs.available_garlands                AS availableGarlands,
            gs.unavailable_garlands              AS unavailableGarlands,
            CONCAT(gs.available_garlands, ' / ', gs.total_garlands, ' items') AS availabilityDisplay,
            COALESCE(comp.profile_completeness_pct, 0)                       AS companyProfilePct
        FROM category_stats cs
        CROSS JOIN garland_stats gs
        LEFT JOIN company_stats comp ON true
        """, nativeQuery = true)
    DashboardOverviewProjection getDashboardOverview();
}
