package com.mari.flora.service.impl;
import com.mari.flora.dto.enums.GarlandRowStatus;
import com.mari.flora.dto.request.GarlandCsvRow;
import com.mari.flora.dto.response.GarlandBulkUploadItemResult;
import com.mari.flora.entity.Category;
import com.mari.flora.entity.Garland;
import com.mari.flora.entity.Image;
import com.mari.flora.exception.GarlandRowProcessingException;
import com.mari.flora.repository.GarlandRepository;
import com.mari.flora.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GarlandRowProcessor {

    private final GarlandRepository garlandRepository;
    private final ImageRepository imageRepository;

    /**
     * Processes exactly one CSV row in its own transaction, independent of
     * other rows in the batch. Throws GarlandRowProcessingException on any
     * validation or persistence failure — the exception propagating out of
     * this proxy-invoked method triggers rollback of ONLY this row's writes.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GarlandBulkUploadItemResult processRow(GarlandCsvRow row, Category category) {
        log.info("[processRow] rowNumber={}, productCode={}", row.getRowNumber(), row.getProductCode());

        List<String> errors = validate(row);
        if (!errors.isEmpty()) {
            String message = String.join("; ", errors);
            log.warn("[processRow] rowNumber={} validation failed: {}", row.getRowNumber(), message);
            throw new GarlandRowProcessingException(row.getRowNumber(), row.getProductCode(), message);
        }

        try {
            BigDecimal price = new BigDecimal(row.getPrice().trim());
            Integer orderBy = StringUtils.hasText(row.getOrderBy()) ? Integer.parseInt(row.getOrderBy().trim()) : 0;
            String extension = row.getImageExtension().trim().toLowerCase().replaceFirst("^\\.", "");
            List<String> materials = splitMaterials(row.getMaterial());

            Optional<Garland> existingOpt = garlandRepository.findByProductCode(row.getProductCode().trim());

            GarlandRowStatus status;
            if (existingOpt.isPresent()) {
                Garland existing = existingOpt.get();
                Image image = existing.getImage();
                if (image == null) {
                    image = Image.builder().build();
                }
                image.setImagePath(row.getImageUrl().trim());
                image.setExtension(extension);
                image = imageRepository.save(image);

                existing.setName(row.getName().trim());
                existing.setDescription(row.getDescription() == null ? null : row.getDescription().trim());
                existing.setOrderBy(orderBy);
                existing.setPrice(price);
                existing.setCategory(category);
                existing.setImage(image);
                existing.getMaterials().clear();
                existing.getMaterials().addAll(materials);

                garlandRepository.save(existing);
                status = GarlandRowStatus.UPDATED;
                log.info("[processRow] rowNumber={} updated garlandId={}", row.getRowNumber(), existing.getId());
            } else {
                Image image = Image.builder()
                        .imagePath(row.getImageUrl().trim())
                        .extension(extension)
                        .build();
                image = imageRepository.save(image);

                Garland garland = Garland.builder()
                        .name(row.getName().trim())
                        .description(row.getDescription() == null ? null : row.getDescription().trim())
                        .orderBy(orderBy)
                        .price(price)
                        .productCode(row.getProductCode().trim())
                        .category(category)
                        .image(image)
                        .materials(new ArrayList<>(materials))
                        .build();

                garland = garlandRepository.save(garland);
                status = GarlandRowStatus.CREATED;
                log.info("[processRow] rowNumber={} created garlandId={}", row.getRowNumber(), garland.getId());
            }

            return GarlandBulkUploadItemResult.builder()
                    .rowNumber(row.getRowNumber())
                    .productCode(row.getProductCode().trim())
                    .status(status)
                    .message(status == GarlandRowStatus.CREATED ? "Garland created" : "Garland updated")
                    .build();

        } catch (GarlandRowProcessingException e) {
            throw e;
        } catch (Exception e) {
            log.error("[processRow] rowNumber={} failed unexpectedly: {}", row.getRowNumber(), e.getMessage(), e);
            throw new GarlandRowProcessingException(row.getRowNumber(), row.getProductCode(), "Unexpected error: " + e.getMessage());
        }
    }

    private List<String> validate(GarlandCsvRow row) {
        List<String> errors = new ArrayList<>();

        if (!StringUtils.hasText(row.getName())) {
            errors.add("name is required");
        }
        if (!StringUtils.hasText(row.getProductCode())) {
            errors.add("product_code is required");
        }
        if (!StringUtils.hasText(row.getImageUrl())) {
            errors.add("image_url is required");
        }
        if (!StringUtils.hasText(row.getImageExtension())) {
            errors.add("image_extension is required");
        }
        if (!StringUtils.hasText(row.getPrice())) {
            errors.add("price is required");
        } else {
            try {
                BigDecimal price = new BigDecimal(row.getPrice().trim());
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    errors.add("price must not be negative");
                }
            } catch (NumberFormatException e) {
                errors.add("price is not a valid number: '" + row.getPrice() + "'");
            }
        }
        if (StringUtils.hasText(row.getOrderBy())) {
            try {
                Integer.parseInt(row.getOrderBy().trim());
            } catch (NumberFormatException e) {
                errors.add("order_by is not a valid integer: '" + row.getOrderBy() + "'");
            }
        }
        return errors;
    }

    private List<String> splitMaterials(String raw) {
        if (!StringUtils.hasText(raw)) {
            return new ArrayList<>();
        }
        return java.util.Arrays.stream(raw.split("\\|"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }
}