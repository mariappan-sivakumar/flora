package com.mari.flora.repository;

import com.mari.flora.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query(value = "select * from category c where :name is null or c.name ilike concat('%', :name, '%') order by c.order", nativeQuery = true)
    Page<Category> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE category SET is_active = false WHERE name = :name", nativeQuery = true)
    int softDeleteByName(@Param("name") String name);
}
