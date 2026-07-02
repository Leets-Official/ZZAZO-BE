package org.example.zzazo.global.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.zzazo.global.common.ApiResponse;
import org.example.zzazo.global.common.HealthResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "서버 상태 확인 API")
@RestController
@RequestMapping("/api")
// 서버 상태 확인 API Controller
public class HealthController {

    // 서버 상태 확인
    @Operation(summary = "헬스 체크", description = "서버가 정상적으로 실행 중인지 확인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "서버 정상 실행 중",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 200,
                              "message": "success",
                              "data": {
                                "status": "UP",
                                "message": "Server is running"
                              }
                            }
                            """))
            )
    })
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<HealthResponseDto>> healthCheck() {
        HealthResponseDto data = HealthResponseDto.builder()
                .status("UP")
                .message("Server is running")
                .build();
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
