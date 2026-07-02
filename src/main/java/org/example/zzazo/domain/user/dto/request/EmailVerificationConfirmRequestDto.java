package org.example.zzazo.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailVerificationConfirmRequestDto {

    @Schema(example = "student@university.ac.kr")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "482910")
    @NotBlank
    @Size(min = 6, max = 6)
    private String verificationCode;
}
