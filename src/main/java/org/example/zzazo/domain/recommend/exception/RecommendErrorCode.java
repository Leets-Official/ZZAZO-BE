package org.example.zzazo.domain.recommend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.global.common.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecommendErrorCode implements BaseCode {


    SEMESTER_NOT_EQUALS(HttpStatus.BAD_REQUEST,"RECOMMEND_400_1","선택한 강의 정보가 올바르지 않습니다."),
    SELECT_DUPLICATED(HttpStatus.BAD_REQUEST,"RECOMMEND_400_2","동일한 과목을 중복 선택할 수 없습니다."),
    SCHEDULE_OVERLAPPED(HttpStatus.BAD_REQUEST,"RECOMMEND_400_3","선택할 수 없는 과목입니다."),
    EXCEED_THIRTY(HttpStatus.BAD_REQUEST,"RECOMMEND_400_","선택한 과목은 30학점을 초과할 수 없습니다."),

    DEPARTMENT_NOT_EXISTS(HttpStatus.NOT_FOUND,"RECOMMEND_404_1","존재하지 않는 학과입니다."),
    LECTURE_NOT_EXISTS(HttpStatus.NOT_FOUND,"RECOMMEND_404_2","존재하지 않는 강의입니다."),
    RECOMMEND_NOT_EXISTS(HttpStatus.NOT_FOUND,"RECOMMEND_404_3","조건에 맞는 시간표가 존재하지 않습니다.");





    private final HttpStatus status;
    private final String code;
    private final String message;
}
