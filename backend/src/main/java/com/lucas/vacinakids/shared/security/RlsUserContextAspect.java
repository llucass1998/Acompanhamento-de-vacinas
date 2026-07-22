package com.lucas.vacinakids.shared.security;

import jakarta.persistence.EntityManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class RlsUserContextAspect {
    private final EntityManager entityManager;

    public RlsUserContextAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Around("execution(* com.lucas.vacinakids..service..*(..)) && @annotation(transactional)")
    public Object setUserContext(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        UUID userId = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof UUID candidate) {
                userId = candidate;
                break;
            }
        }
        if (userId != null) {
            entityManager.createNativeQuery("select set_config('app.current_user_id', :userId, true)")
                    .setParameter("userId", userId.toString())
                    .getSingleResult();
        }
        return joinPoint.proceed();
    }
}
