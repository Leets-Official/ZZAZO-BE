package org.example.zzazo.domain.timetable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.lectureschedule.entity.LectureSchedule;
import org.example.zzazo.global.common.Week;

import java.time.LocalTime;
import java.util.List;

@Schema(description = "시간표에 포함된 과목 정보")
public record TimetableCourseResponse(
        @Schema(description = "강의 ID", example = "13")
        Long lectureId,

        @Schema(description = "과목명", example = "경영학원론")
        String lectureName,

        @Schema(description = "분반", example = "001")
        String section,

        @Schema(description = "교수명", example = "홍길동")
        String professor,

        @Schema(description = "학점", example = "3")
        int credit,

        @Schema(description = "이수구분", example = "전공필수")
        String lectureClassification,

        @Schema(description = "강의실", example = "가천관 000호")
        String classroom,

        @Schema(description = "강의시간")
        List<LectureTime> lectureTime
) {
    public record LectureTime(
            @Schema(description = "시작 시간", example = "09:00")
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,

            @Schema(description = "종료 시간", example = "10:15")
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime,

            @Schema(description = "요일", example = "MON")
            Week dayOfWeek
    ) {

    }

    public static TimetableCourseResponse from(Lecture lecture) {
        return new TimetableCourseResponse(
                lecture.getId(),
                lecture.getName(),
                null,
                lecture.getProfessor(),
                lecture.getCredit(),
                lecture.getLectureClassification().getValue(),
                lecture.getClassroom(),
                lecture.getLectureSchedules().stream()
                        .map(TimetableCourseResponse::from)
                        .toList()
        );
    }

    private static LectureTime from(LectureSchedule lectureSchedule) {
        return new LectureTime(
                lectureSchedule.getStartTime(),
                lectureSchedule.getEndTime(),
                lectureSchedule.getDayOfWeek()
        );
    }

    public static TimetableCourseResponse example() {
        return new TimetableCourseResponse(
                13L,
                "경영학원론",
                "001",
                "홍길동",
                3,
                "전공필수",
                "가천관 000호",
                List.of(
                        new LectureTime(LocalTime.of(9, 0), LocalTime.of(10, 15), Week.MON),
                        new LectureTime(LocalTime.of(10, 30), LocalTime.of(11, 45), Week.WED)
                )
        );
    }
}
