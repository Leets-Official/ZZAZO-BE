package org.example.zzazo.domain.timetable.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.zzazo.domain.lecture.domain.LectureClassification;
import org.example.zzazo.domain.lecture.domain.LiberalCategory;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.timetable.entity.Timetable;

import java.util.List;

@Schema(description = "수강기준 점검 결과")
public record RequirementCheckResponse(
        @Schema(description = "목표 학점 충족 여부", example = "true")
        boolean targetCreditSatisfied,

        @Schema(description = "필수 과목 포함 여부", example = "true")
        boolean requiredCourseIncluded,

        @Schema(description = "기초/계열기초 과목 포함 여부", example = "true")
        boolean foundationCourseIncluded,

        @Schema(description = "교양 과목 포함 여부", example = "true")
        boolean generalEducationIncluded
) {

    /**
     * 수강 기준 점검 결과는 DB에 저장하지 않고, 시간표 상세 조회 시 저장된 강의 정보를 기준으로 계산한다.
     */
    public static RequirementCheckResponse from(Timetable timetable, List<Lecture> lectures) {
        // 현재는 전공필수 또는 교양필수 과목이 하나라도 포함되어 있으면 필수 과목을 포함한 것으로 판단한다.
        boolean requiredCourseIncluded = lectures.stream()
                .map(Lecture::getLectureClassification)
                .anyMatch(classification -> classification == LectureClassification.MAJOR_REQUIREMENT
                        || classification == LectureClassification.LIBERAL_REQUIREMENT);

        // 현재 도메인에서 기초 과목을 구분할 수 있는 값이 AI_BASIC뿐이므로 이를 임시 판정 기준으로 사용한다.
        boolean foundationCourseIncluded = lectures.stream()
                .map(Lecture::getLiberalCategory)
                .anyMatch(category -> category == LiberalCategory.AI_BASIC);

        // 현재는 교양필수 또는 교양선택 과목이 하나라도 포함되어 있으면 교양 과목을 포함한 것으로 판단한다.
        boolean generalEducationIncluded = lectures.stream()
                .map(Lecture::getLectureClassification)
                .anyMatch(classification -> classification == LectureClassification.LIBERAL_REQUIREMENT
                        || classification == LectureClassification.LIBERAL_ELECTIVE);

        return new RequirementCheckResponse(
                // 목표 학점 충족 여부는 저장된 총 학점과 목표 학점을 비교해 판단한다.
                timetable.getTotalCredits() >= timetable.getTargetCredits(),
                requiredCourseIncluded,
                foundationCourseIncluded,
                generalEducationIncluded
        );
    }

    public static RequirementCheckResponse example() {
        return new RequirementCheckResponse(true, true, true, true);
    }
}
