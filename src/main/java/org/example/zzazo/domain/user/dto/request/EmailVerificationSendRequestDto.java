package org.example.zzazo.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailVerificationSendRequestDto {

    @Schema(example = "student@university.ac.kr")
    @NotBlank
    @Email
    private String email;
}
