package com.mari.flora.dto.response;

import com.mari.flora.dto.enums.GarlandRowStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Outcome of processing a single CSV row")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GarlandBulkUploadItemResult {

    @Schema(description = "1-based row number in the CSV (header excluded)", example = "2")
    private int rowNumber;

    @Schema(description = "Product code from the row, if readable", example = "IM-WED-001")
    private String productCode;

    @Schema(description = "Result status for this row")
    private GarlandRowStatus status;

    @Schema(description = "Detail message — error reason if FAILED", example = "Row processed successfully")
    private String message;
}