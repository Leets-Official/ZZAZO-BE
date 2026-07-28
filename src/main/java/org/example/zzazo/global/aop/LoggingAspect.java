package org.example.zzazo.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* org.example.zzazo..service..*(..))")
    public void servicePointcut() {
    }

    // 서비스 실행시간 로깅
    @Around("servicePointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();

            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            log.info("[ExecutionTime] {}.{} - {}ms",
                    className,
                    methodName,
                    end-start);
        }
    }
}
