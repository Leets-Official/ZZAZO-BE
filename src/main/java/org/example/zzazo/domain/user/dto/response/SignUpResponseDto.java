package org.example.zzazo.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.zzazo.domain.user.enums.Role;

@Getter
@Builder
public class SignUpResponseDto {

    private Long userId;
    private String email;
    private int grade;
    private Long departmentId;
    private Long studentId;
    private Role role;
}
