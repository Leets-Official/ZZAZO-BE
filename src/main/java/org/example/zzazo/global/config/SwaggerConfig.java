package org.example.zzazo.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

        @Configuration
        public class SwaggerConfig {

            @Bean
            public OpenAPI zzazoOpenAPI() {
                String description = """
                ZZAZO API 명세서입니다.
                
                
                ### 공통 응답 포맷
                모든 응답은 아래와 같은 공통 포맷으로 반환됩니다:
                ```json
                {
                  "isSuccess": true,
                  "code": "COMMON_200_1",
                  "message": "요청 응답 성공",
                  "data": {
                    "userId": 1
                  }
                }
                ```
               
                """;


                Info info = new Info()
                        .title("ZZAZO API 명세서")
                        .description(description)
                        .version("v1.0.0");

                // Security 설정 (JWT Bearer Token)
                String jwtSchemeName = "jwtAuth";
                SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
                Components components = new Components()
                        .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                                .name(jwtSchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT 인증 토큰을 입력해 주세요. (예: Bearer <JWT_TOKEN>)"));

                return new OpenAPI()
                        .info(info)
                        .addSecurityItem(securityRequirement)
                        .components(components);
            }
        }

