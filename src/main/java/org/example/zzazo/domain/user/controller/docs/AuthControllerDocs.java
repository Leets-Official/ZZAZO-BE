package org.example.zzazo.domain.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.zzazo.domain.user.dto.UserRequest;
import org.example.zzazo.domain.user.dto.UserResponse;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;

// Auth 관련 API의 Swagger 문서 정의
@Tag(name = "Auth", description = "인증 API (이메일 인증, 회원가입, 로그인, 로그아웃, 토큰 재발급)")
public interface AuthControllerDocs {

    @Operation(
            summary = "이메일 인증번호 발송 (회원가입 1단계)",
            description = """
                    회원가입 화면에서 사용자가 입력한 가천대학교 이메일로 6자리 인증번호를 발송합니다.

                    이메일 도메인은 반드시 @gachon.ac.kr 이어야 합니다.
                    학교 이메일 형식이 아니면 인증번호를 발송하지 않습니다.

                    [회원가입 전체 흐름]
                    1단계 - 가천대학교 이메일 입력 후 인증번호 발송 (현재 API)
                    2단계 - 이메일 인증번호 확인 (/api/v1/auth/email/verify)
                    3단계 - 나머지 정보 입력 후 최종 회원가입 (/api/v1/auth/signup)

                    이 API만으로는 회원가입이 완료되지 않습니다.
                    이미 가입된 이메일인 경우 인증번호가 발송되지 않습니다.
                    이미 인증 요청 기록이 있는 이메일이면 새 인증번호와 만료 시간으로 갱신됩니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "인증번호를 발송할 가천대학교 이메일 정보",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "email": "student@gachon.ac.kr"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "인증번호 발송 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (학교 이메일이 아니거나 이메일 형식 오류)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_400_1",
                              "message": "가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다.",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 가입된 이메일",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_409_1",
                              "message": "이미 존재하는 이메일입니다.",
                              "data": null
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<Void>> sendEmailVerification(UserRequest.EmailVerificationSendRequest request);

    @Operation(
            summary = "이메일 인증번호 확인 (회원가입 2단계)",
            description = """
                    회원가입 화면에서 입력한 이메일과 인증번호를 검증합니다.

                    인증 성공 시 해당 이메일은 회원가입 가능한 인증 완료 상태가 됩니다.
                    이후 회원가입 API(/api/auth/signup) 호출 시 동일한 이메일을 사용해야 합니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "인증을 확인할 이메일과 발송받은 6자리 인증번호",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "email": "student@gachon.ac.kr",
                      "verificationCode": "482910"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "인증 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "인증 실패 또는 잘못된 요청 (인증번호 불일치 / 인증 요청 기록 없음 / 인증번호 만료)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "인증번호 불일치", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_400_2",
                                      "message": "인증번호가 일치하지 않습니다.",
                                      "data": null
                                    }
                                    """),
                            @ExampleObject(name = "인증 요청 기록 없음", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_400_5",
                                      "message": "인증 요청 기록이 없습니다. 이메일 인증을 다시 요청해주세요.",
                                      "data": null
                                    }
                                    """),
                            @ExampleObject(name = "인증번호 만료", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_400_6",
                                      "message": "인증번호가 만료되었습니다. 인증번호를 다시 요청해주세요.",
                                      "data": null
                                    }
                                    """)
                    })
            )
    })
    ResponseEntity<ApiResponse<Void>> verifyEmailCode(UserRequest.EmailVerificationConfirmRequest request);

    @Operation(
            summary = "회원가입 (회원가입 3단계)",
            description = """
                    이메일 인증 완료 후 최종 회원가입을 진행합니다.

                    요청의 email 필드는 사용자가 직접 다시 입력하는 값이 아닙니다.
                    회원가입 화면에서 이미 인증 완료된 가천대학교 이메일(@gachon.ac.kr) 값을 그대로 포함해 전송합니다.

                    백엔드는 해당 이메일이 인증 완료 상태인지 확인한 뒤 가입을 처리합니다.
                    이메일 인증이 완료되지 않았거나 학교 이메일(@gachon.ac.kr)이 아닌 경우 가입이 거부됩니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "회원가입에 필요한 사용자 정보. email은 인증 완료된 값이어야 합니다.",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "email": "student@gachon.ac.kr",
                      "password": "password123!",
                      "grade": 2,
                      "departmentId": 3,
                      "studentId": 20210001
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_201_1",
                              "message": "요청 리소스 생성 성공",
                              "data": {
                                "userId": 1,
                                "email": "@gachon.ac.kr",
                                "grade": 2,
                                "departmentId": 3,
                                "studentId": 20210001
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (입력값 검증 실패 또는 학교 이메일 형식 오류)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "입력값 검증 실패", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "COMMON_400_2",
                                      "message": "입력값이 올바르지 않습니다.",
                                      "data": {
                                        "password": "size must be between 8 and 20"
                                      }
                                    }
                                    """),
                            @ExampleObject(name = "학교 이메일 형식 오류", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_400_1",
                                      "message": "가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다.",
                                      "data": null
                                    }
                                    """)
                    })
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "이메일 인증 미완료",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_403_1",
                              "message": "이메일 인증이 완료되지 않았습니다.",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 이메일",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_409_1",
                              "message": "이미 존재하는 이메일입니다.",
                              "data": null
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<UserResponse.SignUpResponse>> signUp(UserRequest.SignUpRequest request);

    @Operation(
            summary = "로그인",
            description = """
                    이메일과 비밀번호로 로그인합니다.

                    로그인에 성공하면 사용자 식별 정보(userId, email)와 함께 accessToken, refreshToken을 발급합니다.
                    refreshToken은 서버에 저장되며, 동일 사용자가 재로그인하면 기존 refreshToken은 새 값으로 갱신됩니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "로그인에 사용할 이메일과 비밀번호",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "email": "student@gachon.ac.kr",
                      "password": "password123!"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "userId": 1,
                                "email": "student@gachon.ac.kr",
                                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.accessTokenExample",
                                "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.refreshTokenExample"
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (입력값 검증 실패)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "COMMON_400_2",
                              "message": "입력값이 올바르지 않습니다.",
                              "data": {
                                "password": "must not be blank"
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "이메일 또는 비밀번호 불일치",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_401_1",
                              "message": "이메일 또는 비밀번호가 일치하지 않습니다.",
                              "data": null
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<UserResponse.LoginResponse>> login(UserRequest.LoginRequest request);

    @Operation(
            summary = "로그아웃",
            description = """
                    클라이언트가 보유한 refreshToken을 서버에 전달하여 로그아웃을 처리합니다.

                    서버는 전달받은 refreshToken의 서명과 만료 여부를 검증한 뒤, DB에 저장된 refreshToken을 조회하여 일치하는 경우 삭제합니다.
                    accessToken은 stateless 방식으로 발급되므로 서버에서 별도로 무효화하지 않습니다.
                    로그아웃 응답을 받으면 클라이언트는 보관 중인 accessToken과 refreshToken을 모두 삭제해야 합니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "로그아웃할 refreshToken",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.refreshTokenExample"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (refreshToken 누락)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "COMMON_400_2",
                              "message": "입력값이 올바르지 않습니다.",
                              "data": {
                                "refreshToken": "공백일 수 없습니다"
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 refreshToken (만료 / 위조·파싱 실패 / DB에 존재하지 않음)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "만료된 refreshToken", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_401_2",
                                      "message": "리프레시 토큰이 만료되었습니다.",
                                      "data": null
                                    }
                                    """),
                            @ExampleObject(name = "위조되었거나 파싱할 수 없는 refreshToken", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_401_3",
                                      "message": "유효하지 않은 리프레시 토큰입니다.",
                                      "data": null
                                    }
                                    """),
                            @ExampleObject(name = "DB에 존재하지 않는 refreshToken (이미 로그아웃됨)", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_401_4",
                                      "message": "이미 로그아웃되었거나 존재하지 않는 리프레시 토큰입니다.",
                                      "data": null
                                    }
                                    """)
                    })
            )
    })
    ResponseEntity<ApiResponse<Void>> logout(UserRequest.LogoutRequest request);

    @Operation(
            summary = "토큰 재발급",
            description = """
                    클라이언트가 보유한 refreshToken으로 새로운 accessToken과 refreshToken을 재발급합니다.

                    서버는 전달받은 refreshToken의 서명과 만료 여부를 검증하고, DB에 저장된 refreshToken과 일치하는지 확인한 뒤
                    토큰에 해당하는 사용자가 실제로 존재하는지 확인합니다.
                    검증에 성공하면 새로운 accessToken과 refreshToken을 발급하며(RefreshToken Rotation),
                    기존 refreshToken은 새 값으로 교체되어 더 이상 사용할 수 없습니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "재발급에 사용할 refreshToken",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.refreshTokenExample"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "재발급 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.newAccessTokenExample",
                                "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.newRefreshTokenExample",
                                "tokenType": "Bearer"
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (refreshToken 누락)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "COMMON_400_2",
                              "message": "입력값이 올바르지 않습니다.",
                              "data": {
                                "refreshToken": "공백일 수 없습니다"
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 refreshToken (만료 / 위조·파싱 실패 / DB에 존재하지 않음 / 사용자 없음)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "만료된 refreshToken", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_401_2",
                                      "message": "리프레시 토큰이 만료되었습니다.",
                                      "data": null
                                    }
                                    """),
                            @ExampleObject(name = "위조되었거나 파싱할 수 없는 refreshToken", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_401_3",
                                      "message": "유효하지 않은 리프레시 토큰입니다.",
                                      "data": null
                                    }
                                    """),
                            @ExampleObject(name = "DB에 존재하지 않는 refreshToken (이미 로그아웃되었거나 이전에 재발급으로 교체됨)", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_401_4",
                                      "message": "이미 로그아웃되었거나 존재하지 않는 리프레시 토큰입니다.",
                                      "data": null
                                    }
                                    """),
                            @ExampleObject(name = "토큰에 해당하는 사용자 없음", value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH_401_5",
                                      "message": "토큰에 해당하는 사용자를 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    })
            )
    })
    ResponseEntity<ApiResponse<UserResponse.TokenReissueResponse>> refresh(UserRequest.RefreshRequest request);
}
