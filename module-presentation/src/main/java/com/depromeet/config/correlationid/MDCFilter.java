package com.depromeet.config.correlationid;

import jakarta.servlet.*;
import java.io.IOException;
import java.util.UUID;
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
        final UUID uuid = UUID.randomUUID();
        String requestId = uuid.toString().replaceAll("-", "");
        MDC.put("request_id", requestId);
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }
}
