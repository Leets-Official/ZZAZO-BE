package org.example.zzazo.global.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.example.zzazo.global.code.BaseErrorCode;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e, HttpServletRequest request) {
        log.warn("[CustomException] {} {} - {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.failure(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity
                .status(BaseErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.failure(BaseErrorCode.VALIDATION_ERROR, errors));
    }

    // RequestBody 예외 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request) {

        log.warn("[HttpMessageNotReadableException] {} {} - Status: 400",
                request.getMethod(), request.getRequestURI(), e);

        Map<String, String> errors = new LinkedHashMap<>();

        switch (e.getCause()) {
            // 1. 데이터 타입 불일치 에러
            case InvalidFormatException ife -> {
                String fieldName = ife.getPath().isEmpty() ? "unknown" : ife.getPath().getLast().getFieldName();

                String rejectedValue = ife.getValue() == null ? "" : ife.getValue().toString();
                String requiredType = ife.getTargetType() == null ? "올바른 타입" : ife.getTargetType().getSimpleName();

                String errorMessage = String.format("'%s' 값은 유효하지 않습니다. (필요 타입: %s)",rejectedValue,requiredType);
                errors.put(fieldName, errorMessage);
            }
            // 2. 데이터 구조 불일치 에러
            case MismatchedInputException mie -> {
                String fieldName = mie.getPath().isEmpty() ? "unknown" : mie.getPath().getLast().getFieldName();
                errors.put(fieldName, "입력 데이터 구조나 타입이 올바르지 않습니다.");
            }
            // 3. Json 문법 에러 및 파싱 실패
            case null, default ->
                    errors.put("payload", "요청 본문(JSON)을 읽을 수 없거나 문법이 올바르지 않습니다.");
        }

        return ResponseEntity
                .status(BaseErrorCode.HTTP_NOT_READABLE.getStatus())
                .body(ApiResponse.failure(BaseErrorCode.HTTP_NOT_READABLE, errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Map<String,String>>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,HttpServletRequest request) {
        log.warn("[MethodArgumentTypeMismatchException] {} {} - Status: 400",
                request.getMethod(),
                request.getRequestURI(),
                e);

        Map<String,String> errors = new LinkedHashMap<>();

        String rejectedValue = e.getValue() == null ? "" : e.getValue().toString();
        String requiredType = e.getRequiredType() == null ? "올바른 타입" : e.getRequiredType().getSimpleName();

        String errorMessage = String.format("'%s' 값은 유효하지 않습니다. (필요 타입: %s)",rejectedValue,requiredType);
        errors.put(e.getName(),errorMessage);

        return ResponseEntity
                .status(BaseErrorCode.TYPE_MISMATCH_ERROR.getStatus())
                .body(ApiResponse.failure(BaseErrorCode.TYPE_MISMATCH_ERROR,errors));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("[Unhandled Exception] {} {} - Status: 500",
                request.getMethod(),
                request.getRequestURI(),
                e);

        return ResponseEntity
                .status(BaseErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.failure(BaseErrorCode.INTERNAL_SERVER_ERROR));
    }
}
