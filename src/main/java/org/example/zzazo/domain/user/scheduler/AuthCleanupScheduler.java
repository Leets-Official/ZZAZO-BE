package org.example.zzazo.domain.user.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zzazo.domain.user.repository.EmailVerificationRepository;
import org.example.zzazo.domain.user.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// 만료된 일회성 인증/토큰 데이터를 주기적으로 정리하는 스케줄러
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCleanupScheduler {

    private final EmailVerificationRepository emailVerificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 매시 정각에 만료된 이메일 인증 기록을 삭제한다.
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanUpExpiredEmailVerifications() {
        int deletedCount = emailVerificationRepository.deleteAllExpired(LocalDateTime.now());
        log.info("[AuthCleanupScheduler] 만료된 이메일 인증 기록 {}건 삭제", deletedCount);
    }

    // 매시 정각에 만료된 리프레시 토큰을 삭제한다.
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanUpExpiredRefreshTokens() {
        int deletedCount = refreshTokenRepository.deleteAllExpired(LocalDateTime.now());
        log.info("[AuthCleanupScheduler] 만료된 리프레시 토큰 {}건 삭제", deletedCount);
    }
}
