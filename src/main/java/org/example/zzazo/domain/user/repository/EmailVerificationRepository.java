package org.example.zzazo.domain.user.repository;

import org.example.zzazo.domain.user.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmail(String email);

    // 만료된 인증 기록 일괄 삭제 (스케줄러용)
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM EmailVerification e WHERE e.expiresAt < :now")
    int deleteAllExpired(@Param("now") LocalDateTime now);
}
