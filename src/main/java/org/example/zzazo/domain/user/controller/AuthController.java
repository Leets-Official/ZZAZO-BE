package org.example.zzazo.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.zzazo.domain.user.dto.request.EmailVerificationConfirmRequestDto;
import org.example.zzazo.domain.user.dto.request.EmailVerificationSendRequestDto;
import org.example.zzazo.domain.user.dto.request.LoginRequestDto;
import org.example.zzazo.domain.user.dto.request.SignUpRequestDto;
import org.example.zzazo.domain.user.dto.response.LoginResponseDto;
import org.example.zzazo.domain.user.dto.response.SignUpResponseDto;
import org.example.zzazo.domain.user.enums.Role;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Operation(
            summary = "이메일 인증번호 발송 (회원가입 1단계)",
            description = """
                    회원가입 화면에서 사용자가 입력한 이메일로 6자리 인증번호를 발송합니다.

                    [회원가입 전체 흐름]
                    1단계 - 이메일 입력 후 인증번호 발송 (현재 API)
                    2단계 - 이메일 인증번호 확인 (/api/auth/email/verify)
                    3단계 - 나머지 정보 입력 후 최종 회원가입 (/api/auth/signup)

                    이 API만으로는 회원가입이 완료되지 않습니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "인증번호 발송 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 200,
                              "message": "인증번호가 발송되었습니다."
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 400,
                              "message": "이메일 형식이 올바르지 않습니다."
                            }
                            """))
            )
    })
    @PostMapping("/email/send")
    public ResponseEntity<ApiResponse<Void>> sendEmailVerification(
            @Valid @RequestBody EmailVerificationSendRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success("인증번호가 발송되었습니다.", null));
    }

    @Operation(
            summary = "이메일 인증번호 확인 (회원가입 2단계)",
            description = """
                    회원가입 화면에서 입력한 이메일과 인증번호를 검증합니다.

                    인증 성공 시 해당 이메일은 회원가입 가능한 인증 완료 상태가 됩니다.
                    이후 회원가입 API(/api/auth/signup) 호출 시 동일한 이메일을 사용해야 합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "인증 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 200,
                              "message": "이메일 인증이 완료되었습니다."
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "인증 실패 또는 잘못된 요청",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 400,
                              "message": "인증번호가 일치하지 않습니다."
                            }
                            """))
            )
    })
    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmailCode(
            @Valid @RequestBody EmailVerificationConfirmRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success("이메일 인증이 완료되었습니다.", null));
    }

    @Operation(
            summary = "회원가입 (회원가입 3단계)",
            description = """
                    이메일 인증 완료 후 최종 회원가입을 진행합니다.

                    요청의 email 필드는 사용자가 직접 다시 입력하는 값이 아닙니다.
                    회원가입 화면에서 이미 인증 완료된 이메일 값을 그대로 포함해 전송합니다.

                    백엔드는 해당 이메일이 인증 완료 상태인지 확인한 뒤 가입을 처리합니다.
                    이메일 인증이 완료되지 않은 경우 가입이 거부됩니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 201,
                              "message": "success",
                              "data": {
                                "userId": 1,
                                "email": "student@university.ac.kr",
                                "grade": 2,
                                "departmentId": 3,
                                "studentId": 20210001,
                                "role": "USER"
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 400,
                              "message": "입력값이 올바르지 않습니다."
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "이메일 인증 미완료",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 403,
                              "message": "이메일 인증이 완료되지 않았습니다."
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 이메일",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 409,
                              "message": "이미 존재하는 이메일입니다."
                            }
                            """))
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> signUp(
            @Valid @RequestBody SignUpRequestDto request) {
        SignUpResponseDto response = SignUpResponseDto.builder()
                .userId(1L)
                .email(request.getEmail())
                .grade(request.getGrade())
                .departmentId(request.getDepartmentId())
                .studentId(request.getStudentId())
                .role(Role.USER)
                .build();
        return ResponseEntity.status(201).body(ApiResponse.success(response));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 200,
                              "message": "success",
                              "data": {
                                "userId": 1,
                                "email": "student@university.ac.kr",
                                "role": "USER"
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 400,
                              "message": "입력값이 올바르지 않습니다."
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "이메일 또는 비밀번호 불일치",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 401,
                              "message": "이메일 또는 비밀번호가 일치하지 않습니다."
                            }
                            """))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = LoginResponseDto.builder()
                .userId(1L)
                .email(request.getEmail())
                .role(Role.USER)
                .build();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "로그아웃", description = "로그인된 사용자를 로그아웃합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 200,
                              "message": "로그아웃 되었습니다."
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 401,
                              "message": "인증되지 않은 사용자입니다."
                            }
                            """))
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success("로그아웃 되었습니다.", null));
    }
}
