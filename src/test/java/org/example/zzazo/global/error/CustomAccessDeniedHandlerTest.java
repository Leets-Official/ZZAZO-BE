package org.example.zzazo.global.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThat;

// 프로젝트에 역할/권한 기반 API가 없어 실제 403 상황을 HTTP 통합 테스트로 재현할 수 없으므로,
// CustomAccessDeniedHandler의 동작을 직접 호출하여 검증한다.
class CustomAccessDeniedHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler(objectMapper);

    @Test
    void 권한없는_요청에_대해_403과_COMMON_403_1_응답을_반환한다() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/example");
        MockHttpServletResponse response = new MockHttpServletResponse();

        customAccessDeniedHandler.handle(request, response, new AccessDeniedException("access is denied"));

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentType()).startsWith("application/json");

        JsonNode body = objectMapper.readTree(response.getContentAsString());
        assertThat(body.get("isSuccess").asBoolean()).isFalse();
        assertThat(body.get("code").asText()).isEqualTo("COMMON_403_1");
        assertThat(body.get("data").isNull()).isTrue();
    }
}
