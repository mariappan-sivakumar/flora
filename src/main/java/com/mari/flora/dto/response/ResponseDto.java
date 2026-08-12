package com.mari.flora.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    private Integer statusCode;
    private String message;
    private T data;
    private String error;
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
