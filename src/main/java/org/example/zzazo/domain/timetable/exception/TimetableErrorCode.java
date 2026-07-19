package org.example.zzazo.domain.timetable.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.global.code.BaseErrorCode;
import org.example.zzazo.global.common.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TimetableErrorCode implements BaseCode {

    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND,"TIMETABLE_404_1","강의를 찾을 수 없습니다.");




    private final HttpStatus status;
    private final String code;
    private final String message;
}
