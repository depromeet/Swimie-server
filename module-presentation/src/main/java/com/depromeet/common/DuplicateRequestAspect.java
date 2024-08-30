package com.depromeet.common;

import static java.util.concurrent.TimeUnit.*;

import com.depromeet.exception.BaseException;
import com.depromeet.friend.dto.request.FollowRequest;
import com.depromeet.type.common.CommonErrorType;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class DuplicateRequestAspect {
    private Set<String> requestSet = new ConcurrentSkipListSet<>();
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

        String requestURI = request.getRequestURI();
        String userId = getUserIdFromSecurityContext();

        String requestId = userId + joinPoint.getSignature().toLongString();
        requestId = handleWhitelist(joinPoint, requestURI, requestId);

        if (requestSet.contains(requestId)) {
            handleDuplicateRequest();
        }
        requestSet.add(requestId);

        try {
            return joinPoint.proceed();
        } finally {
            final String finalRequestId = requestId;
            scheduler.schedule(() -> requestSet.remove(finalRequestId), 1, SECONDS);
        }
    }

    private String handleWhitelist(
            ProceedingJoinPoint joinPoint, String requestURI, String requestId) {
        if (requestURI.equals("/friend")) {
            Object[] args = joinPoint.getArgs();
            Object requestBody = null;

            for (Object arg : args) {
                if (arg instanceof FollowRequest) {
                    requestBody = arg;
                    break;
                }
            }

            if (requestBody != null) {
                FollowRequest requests = (FollowRequest) requestBody;
                requestId += requests.followingId().toString();
            }
        }
        return requestId;
    }

    private void handleDuplicateRequest() {
        throw new BaseException(CommonErrorType.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS);
    }

    private String getUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }
        }
        return null;
    }
}
