package org.example.zzazo.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
// API 공통 응답 래퍼
public class ApiResponse<T> {

    @Schema(example = "200")
    private int status;

    @Schema(example = "success")
    private String message;

    private T data;

    // 데이터 포함 성공 응답 생성
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message("success")
                .data(data)
                .build();
    }

    // 메시지와 데이터 포함 성공 응답 생성
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .build();
    }
}
