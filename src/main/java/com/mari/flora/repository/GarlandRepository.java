package com.mari.flora.repository;

import com.mari.flora.entity.Garland;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GarlandRepository extends JpaRepository<Garland, Long> {
    Optional<Garland> findByProductCode(String productCode);
    @Query(
            value = "SELECT DISTINCT g.* FROM garland g " +
                    "JOIN category c ON g.category_id = c.id " +
                    "LEFT JOIN garland_material gm ON g.id = gm.garland_id " +
                    "WHERE g.is_active = true " +
                    "AND (:search IS NULL OR g.name ILIKE CONCAT('%', :search, '%') " +
                    "     OR g.description ILIKE CONCAT('%', :search, '%')) " +
                    "AND (:productCode IS NULL OR g.product_code ILIKE CONCAT('%', :productCode, '%')) " +
                    "AND (:categoryName IS NULL OR c.name = :categoryName) " +
                    "AND (CAST(:flowers AS text[]) IS NULL OR gm.material = ANY(CAST(:flowers AS text[]))) " + // changed: cast both occurrences
                    "AND (:minPrice IS NULL OR g.price >= :minPrice) " +
                    "AND (:maxPrice IS NULL OR g.price <= :maxPrice) " +
                    "AND (:active IS NULL OR g.is_active = :active)",
            countQuery = "SELECT COUNT(DISTINCT g.id) FROM garland g " +
                    "JOIN category c ON g.category_id = c.id " +
                    "LEFT JOIN garland_material gm ON g.id = gm.garland_id " +
                    "WHERE g.is_active = true " +
                    "AND (:search IS NULL OR g.name ILIKE CONCAT('%', :search, '%') " +
                    "     OR g.description ILIKE CONCAT('%', :search, '%')) " +
                    "AND (:productCode IS NULL OR g.product_code ILIKE CONCAT('%', :productCode, '%')) " +
                    "AND (:categoryName IS NULL OR c.name = :categoryName) " +
                    "AND (CAST(:flowers AS text[]) IS NULL OR gm.material = ANY(CAST(:flowers AS text[]))) " + // changed: cast both occurrences
                    "AND (:minPrice IS NULL OR g.price >= :minPrice) " +
                    "AND (:maxPrice IS NULL OR g.price <= :maxPrice) " +
                    "AND (:active IS NULL OR g.is_active = :active)",
            nativeQuery = true
    )
    Page<Garland> searchGarlands(
            @Param("search") String search,
            @Param("productCode") String productCode,
            @Param("categoryName") String categoryName,
            @Param("flowers") String[] flowers,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("active") Boolean active,
            Pageable pageable);

    Page<Garland> findByCategoryName(String categoryName, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE garland SET is_active = false WHERE product_code = :productCode", nativeQuery = true)
    int softDeleteByProductCode(String productCode);

    @Query(value = "SELECT DISTINCT gm.material FROM garland_material gm", nativeQuery = true)
    List<String> findDistinctFlowers();
}
