package org.example.zzazo.global.error;

import lombok.Getter;
import org.example.zzazo.global.common.BaseCode;

// BaseCode 기반 도메인 비즈니스 예외
@Getter
public class CustomException extends RuntimeException {

    private final BaseCode errorCode;

    public CustomException(BaseCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
