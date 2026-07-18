package org.example.zzazo.domain.timetable.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.example.zzazo.global.common.Week;

import java.util.List;

@Schema(description = "시간표 저장 요청")
public record TimetableCreateRequest(
        @Schema(description = "후보명", example = "공강 조건 중심 시간표")
        @NotBlank(message = "후보명은 필수입니다.")
        String candidateName,

        @Schema(description = "학과 ID", example = "1")
        @NotNull(message = "학과 정보는 필수입니다.")
        Long departmentId,

        @Schema(description = "학기", example = "2", allowableValues = {"1", "2"})
        @NotNull(message = "학기 정보는 필수입니다.")
        @Min(value = 1, message = "학기는 1 또는 2만 입력 가능합니다.")
        @Max(value = 2, message = "학기는 1 또는 2만 입력 가능합니다.")
        Integer semester,

        @Schema(description = "학년", example = "1")
        @NotNull(message = "학년 정보는 필수입니다.")
        @Min(value = 1, message = "학년은 1~4학년만 가능합니다.")
        @Max(value = 4, message = "학년은 1~4학년만 가능합니다.")
        Integer grade,

        @ArraySchema(schema = @Schema(description = "희망 공강 요일", example = "FRI"))
        @Size(max = 2, message = "공강 요일은 최대 2개까지 선택할 수 있습니다.")
        List<Week> preferredFreeDays,

        @Schema(description = "목표 학점", example = "18")
        @NotNull(message = "목표 학점은 필수입니다.")
        @PositiveOrZero(message = "목표 학점은 0 이상이어야 합니다.")
        Integer targetCredits,

        @ArraySchema(schema = @Schema(description = "저장할 시간표에 포함된 강의 ID", example = "13"))
        List<Long> selectedLectureIds,

        @Schema(description = "총 학점", example = "20")
        @NotNull(message = "총 학점은 필수입니다.")
        @PositiveOrZero(message = "총 학점은 0 이상이어야 합니다.")
        Integer totalCredits
) {
}
