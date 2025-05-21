package com.example.obs.core.exceptions;

import com.example.obs.core.utilities.results.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception exception) {
        return new ResponseEntity<>(ApiResponse.error("Bir hata oluştu: " + exception.getMessage()), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<String>> handleNoSuchElementException(NoSuchElementException exception) {
        return new ResponseEntity<>(ApiResponse.error("Kayıt bulunamadı: " + exception.getMessage()),
                                    HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()),
                                    HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<String>> handleBusinessException(BusinessException exception) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthorizationException(AuthorizationException exception) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()),
                                    HttpStatus.FORBIDDEN);
    }


    


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity<>(ApiResponse.error("Geçersiz istek: " + exception.getMessage()),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException exception) {
        return new ResponseEntity<>(ApiResponse.error("Erişim reddedildi: Yetkiniz bulunmamaktadır."),
                                    HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(new ApiResponse<>(errors, false, "Doğrulama hatası"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(ValidationException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getErrors(), false, exception.getMessage()), 
                                   HttpStatus.BAD_REQUEST);
    }
}
