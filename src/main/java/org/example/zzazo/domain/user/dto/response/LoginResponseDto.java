package org.example.zzazo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
// 로그인 응답 정보
public class LoginResponseDto {

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "student@university.ac.kr")
    private String email;

}
