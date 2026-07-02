package org.example.zzazo.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.zzazo.domain.user.enums.Role;

@Getter
@Builder
public class LoginResponseDto {

    private Long userId;
    private String email;
    private Role role;
}
