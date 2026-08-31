package com.mari.flora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "garland")
// ✅ FIX: replaced @Data with @Getter/@Setter + explicit equals/hashCode/toString
// scoped to 'id' only. @Data was including the 'category' back-reference,
// which is the other half of the circular hashCode chain with Category.
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garland {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // ✅ FIX
    @ToString.Include // ✅ FIX
    private Integer id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "product_code", nullable = false, length = 50, unique = true)
    private String productCode;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_by")
    @Builder.Default
    private Integer orderBy = 0;

    // ✅ FIX: NOT included in equals/hashCode/toString anymore — this was the
    // other side of the circular reference with Category.garlands.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_garland_category_id"))
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "fk_garland_image_id"))
    private Image image;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "garland_material",
            joinColumns = @JoinColumn(name = "garland_id", foreignKey = @ForeignKey(name = "fk_garland_material_garland_id"))
    )
    @Column(name = "material", nullable = false, length = 100)
    @Builder.Default
    private List<String> materials = new ArrayList<>();
}