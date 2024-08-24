package com.depromeet.common;

import static java.util.concurrent.TimeUnit.*;

import com.depromeet.exception.BaseException;
import com.depromeet.type.common.CommonErrorType;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class DuplicateRequestAspect {
    private Set<String> requestSet = Collections.synchronizedSet(new HashSet<>());
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Pointcut("within(*..*Controller)")
    public void onRequest() {}

    @Around("onRequest()")
    public Object duplicateRequestCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String httpMethod = request.getMethod();

        if ("GET".equalsIgnoreCase(httpMethod)) {
            return joinPoint.proceed();
        }

        String requestId = joinPoint.getSignature().toLongString();
        if (requestSet.contains(requestId)) {
            handleDuplicateRequest();
        }
        requestSet.add(requestId);
        try {
            return joinPoint.proceed();
        } finally {
            scheduler.schedule(() -> requestSet.remove(requestId), 1, SECONDS);
        }
    }

    private void handleDuplicateRequest() {
        throw new BaseException(CommonErrorType.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS);
    }
}
