package com.mari.flora.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GarlandCsvRow {
    private int rowNumber;      // 1-based, accounts for header row
    private String name;
    private String description;
    private String orderBy;
    private String price;
    private String productCode;
    private String imageUrl;
    private String imageExtension;
    private String material;    // pipe-separated, e.g. "rose|lotus"
}