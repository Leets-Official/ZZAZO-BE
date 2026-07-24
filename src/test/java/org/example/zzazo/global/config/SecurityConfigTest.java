package org.example.zzazo.global.config;

import org.example.zzazo.domain.user.entity.User;
import org.example.zzazo.domain.user.repository.UserRepository;
import org.example.zzazo.global.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    // 인증 정보 없이 인증 필수 API 접근 시 CustomAuthenticationEntryPoint가 COMMON_401_1을 반환하는지 확인
    @Test
    void 인증정보없이_인증필수_API에_접근하면_401과_COMMON_401_1을_반환한다() throws Exception {
        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON_401_1"));
    }

    // permitAll 경로는 기존처럼 인증 없이 접근 가능해야 한다
    @Test
    void permitAll_경로는_인증없이_기존처럼_접근가능하다() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk());
    }

    // 유효한 Access Token으로 인증 필수 API에 접근하면 기존처럼 정상 동작해야 한다
    @Test
    void 유효한_AccessToken으로_인증필수_API에_접근하면_기존처럼_성공한다() throws Exception {
        User user = userRepository.save(User.builder()
                .departmentId(1L)
                .studentId(20210001L)
                .email("security-test@gachon.ac.kr")
                .password("encoded-password")
                .grade(1)
                .emailVerified(true)
                .build());

        String accessToken = jwtProvider.createAccessToken(user.getUserId(), user.getEmail());

        mockMvc.perform(get("/api/v1/departments")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON_200_1"));
    }
}
