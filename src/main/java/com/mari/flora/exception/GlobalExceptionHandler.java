package com.mari.flora.exception;

import com.mari.flora.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ResponseDto<String>> handleInvalidFile(InvalidFileException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseDto<String>> handleMaxSize(MaxUploadSizeExceededException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Uploaded file exceeds the configured multipart size limit");
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ResponseDto<String>> handleFileStorage(FileStorageException ex) {
        log.error("File storage failure", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not process the uploaded file");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<String>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse("Validation failed");
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(GarlandRowProcessingException.class)
    public ResponseEntity<ResponseDto<String>> handleGarlandRowProcessing(GarlandRowProcessingException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(FunctionalException.class)
    public ResponseEntity<ResponseDto<String>> handleFunctionalException(FunctionalException ex) {
        return buildResponse(ex.getStatus(), ex.getMessage());
    }

    private ResponseEntity<ResponseDto<String>> buildResponse(HttpStatus status, String errorMessage) {
        
        return ResponseEntity.status(status).body(ResponseDto.error(status.value(), errorMessage, status.getReasonPhrase()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<String>> handleException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
