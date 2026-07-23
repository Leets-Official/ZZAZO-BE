package org.example.zzazo.domain.lecture.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.global.common.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum LectureErrorCode implements BaseCode {

    DEPARTMENT_NOT_EXISTS(HttpStatus.NOT_FOUND,"LECTURE_404_1","존재하지 않는 학과입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
