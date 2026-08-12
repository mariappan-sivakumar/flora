package com.mari.flora.repository;

import com.mari.flora.entity.Garland;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GarlandRepository extends JpaRepository<Garland, Long> {
    Optional<Garland> findByProductCode(String productCode);
    @Query(
            value = "SELECT g.* FROM garland g " +
                    "JOIN category c ON g.category_id = c.id " +
                    "WHERE g.is_active = true " +
                    "AND (:search IS NULL OR g.name ILIKE CONCAT('%', :search, '%') " +
                    "     OR g.description ILIKE CONCAT('%', :search, '%')) " +
                    "AND (:productCode IS NULL OR g.product_code ILIKE CONCAT('%', :productCode, '%')) " +
                    "AND (:categoryName IS NULL OR c.name = :categoryName)",
            countQuery = "SELECT count(g.id) FROM garland g " +
                    "JOIN category c ON g.category_id = c.id " +
                    "WHERE g.is_active = true " +
                    "AND (:search IS NULL OR g.name ILIKE CONCAT('%', :search, '%') " +
                    "     OR g.description ILIKE CONCAT('%', :search, '%')) " +
                    "AND (:productCode IS NULL OR g.product_code ILIKE CONCAT('%', :productCode, '%')) " +
                    "AND (:categoryName IS NULL OR c.name = :categoryName)",
            nativeQuery = true
    )
    Page<Garland> searchGarlands(
            @Param("search") String search,
            @Param("productCode") String productCode,
            @Param("categoryName") String categoryName,
            Pageable pageable);

    Page<Garland> findByCategoryName(String categoryName, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE garland SET is_active = false WHERE product_code = :productCode", nativeQuery = true)
    int softDeleteByProductCode(String productCode);
}
