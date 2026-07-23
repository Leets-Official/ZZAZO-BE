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

    public static RequirementCheckResponse from(Timetable timetable, List<Lecture> lectures) {
        boolean requiredCourseIncluded = lectures.stream()
                .map(Lecture::getLectureClassification)
                .anyMatch(classification -> classification == LectureClassification.MAJOR_REQUIREMENT
                        || classification == LectureClassification.LIBERAL_REQUIREMENT);
        boolean foundationCourseIncluded = lectures.stream()
                .map(Lecture::getLiberalCategory)
                .anyMatch(category -> category == LiberalCategory.AI_BASIC);
        boolean generalEducationIncluded = lectures.stream()
                .map(Lecture::getLectureClassification)
                .anyMatch(classification -> classification == LectureClassification.LIBERAL_REQUIREMENT
                        || classification == LectureClassification.LIBERAL_ELECTIVE);

        return new RequirementCheckResponse(
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
