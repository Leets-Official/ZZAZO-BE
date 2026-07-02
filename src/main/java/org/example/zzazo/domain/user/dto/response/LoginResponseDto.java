package org.example.zzazo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.example.zzazo.domain.user.enums.Role;

@Getter
@Builder
public class LoginResponseDto {

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "student@university.ac.kr")
    private String email;

    @Schema(example = "USER")
    private Role role;
}
