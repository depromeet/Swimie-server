package com.depromeet.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Configuration
public class LoggerConfig {
    @Around("@annotation(com.depromeet.config.Logging) && @annotation(logging)")
    public Object aroundLogger(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        RequestLog requestLog = new RequestLog();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        requestLog.setUri(request.getRequestURI());

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            throw t;
        } finally {
            if (result == null) requestLog.setResult("fail");
            else requestLog.setResult("success");

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            requestLog.setMethod(methodSignature.getName());
            requestLog.setItem(logging.item());
            requestLog.setAction(logging.action());

            log.info(getMessage(requestLog));
        }
        return result;
    }

    private String getMessage(RequestLog requestLog) throws JsonProcessingException {
        Map<String, String> map = new LinkedHashMap<>();

        map.put("item", requestLog.getItem());
        map.put("action", requestLog.getAction());
        map.put("result", requestLog.getResult());
        map.put("uri", requestLog.getUri());
        map.put("method", requestLog.getMethod());

        return new ObjectMapper().writeValueAsString(map);
    }
}
