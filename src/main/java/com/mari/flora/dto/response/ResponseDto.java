package com.mari.flora.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Standard API response wrapper for all endpoint responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    @Schema(description = "HTTP status code returned by the API", example = "200")
    private Integer statusCode;
    @Schema(description = "Success or status message", example = "Operation successful")
    private String message;
    @Schema(description = "Actual payload returned by the endpoint")
    private T data;
    @Schema(description = "Error detail when the request failed", example = "Request validation failed")
    private String error;
    @Schema(description = "Unix timestamp in milliseconds for the response", example = "1726165800000")
    private Long timestamp;

    public static <T> ResponseDto<T> success(T data, String message) {
        return success(data, message, 200);
    }

    public static <T> ResponseDto<T> success(T data, String message, Integer statusCode) {
        return ResponseDto.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ResponseDto<T> success(T data) {
        return success(data, "Operation successful");
    }

    public static <T> ResponseDto<T> error(Integer statusCode, String error) {
        return ResponseDto.<T>builder()
                .statusCode(statusCode)
                .error(error)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ResponseDto<T> error(Integer statusCode, String error, String message) {
        return ResponseDto.<T>builder()
                .statusCode(statusCode)
                .error(error)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
