package org.example.zzazo.domain.timetable.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.global.common.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TimetableErrorCode implements BaseCode {

    TIMETABLE_ACCESS_DENIED(
            HttpStatus.FORBIDDEN,
            "TIMETABLE_403_1",
            "다른 사용자의 시간표에 접근할 수 없습니다."
    ),
    TIMETABLE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "TIMETABLE_404_1",
            "시간표를 찾을 수 없습니다."
    );

    private final HttpStatus status;
    private final String code;
    private final String message;
}
