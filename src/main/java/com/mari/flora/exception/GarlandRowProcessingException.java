package com.mari.flora.exception;

import lombok.Getter;

@Getter
public class GarlandRowProcessingException extends RuntimeException {

    private final int rowNumber;
    private final String productCode;

    public GarlandRowProcessingException(int rowNumber, String productCode, String message) {
        super(message);
        this.rowNumber = rowNumber;
        this.productCode = productCode;
    }
}