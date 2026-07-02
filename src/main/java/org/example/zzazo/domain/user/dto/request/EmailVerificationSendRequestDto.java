package org.example.zzazo.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
// 이메일 인증번호 발송 요청 정보
public class EmailVerificationSendRequestDto {

    @Schema(
            description = "가천대학교 이메일 주소. 도메인은 반드시 @gachon.ac.kr 이어야 합니다.",
            example = "student@gachon.ac.kr"
    )
    @NotBlank
    @Email
    @Pattern(
            regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@gachon\\.ac\\.kr$",
            message = "가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다."
    )
    private String email;
}
