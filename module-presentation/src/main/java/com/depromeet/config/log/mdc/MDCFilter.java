package com.depromeet.config.log.mdc;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        String clientIp = servletRequest.getRemoteAddr();
        MDC.put("request_id", getClientIp(servletRequest));
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }

    private String getClientIp(ServletRequest request) {
        String clientIp = request.getRemoteAddr();

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String xForwardedFor = httpServletRequest.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                clientIp = xForwardedFor.split(",")[0].trim();
            }
        }

        return clientIp;
    }
}
