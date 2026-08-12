package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Summary and per-row results of a garland bulk CSV upload")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GarlandBulkUploadResponse {

    @Schema(description = "Total data rows read from the CSV", example = "25")
    private int totalRows;

    @Schema(description = "Rows successfully created or updated", example = "23")
    private int successCount;

    @Schema(description = "Rows that failed validation or persistence", example = "2")
    private int failureCount;

    @Schema(description = "Per-row outcome, in file order")
    private List<GarlandBulkUploadItemResult> results;
}