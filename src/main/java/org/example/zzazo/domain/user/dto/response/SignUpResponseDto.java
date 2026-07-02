package org.example.zzazo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
// 회원가입 응답 정보
public class SignUpResponseDto {

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "student@university.ac.kr")
    private String email;

    @Schema(example = "2")
    private int grade;

    @Schema(example = "3")
    private Long departmentId;

    @Schema(example = "20210001")
    private Long studentId;

}
