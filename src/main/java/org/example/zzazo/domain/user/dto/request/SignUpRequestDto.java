package org.example.zzazo.domain.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotNull
    private Long departmentId;

    @NotNull
    private Long studentId;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    @Min(1)
    @Max(4)
    private Integer grade;
}
