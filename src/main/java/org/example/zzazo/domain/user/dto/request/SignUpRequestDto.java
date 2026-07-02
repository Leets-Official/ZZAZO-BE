package org.example.zzazo.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
// 회원가입 요청 정보
public class SignUpRequestDto {

    @Schema(example = "3")
    @NotNull
    private Long departmentId;

    @Schema(example = "20210001")
    @NotNull
    private Long studentId;

    @Schema(
            description = "인증 완료된 가천대학교 이메일(@gachon.ac.kr). 사용자가 재입력하는 값이 아니라 회원가입 화면에서 인증 완료된 이메일 값을 그대로 포함해 전송합니다.",
            example = "student@gachon.ac.kr"
    )
    @NotBlank
    @Email
    @Pattern(
            regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@gachon\\.ac\\.kr$",
            message = "가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다."
    )
    private String email;

    @Schema(example = "password123!")
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @Schema(example = "2")
    @NotNull
    @Min(1)
    @Max(4)
    private Integer grade;
}
