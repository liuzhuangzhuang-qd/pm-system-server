package com.pm.common.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * HTTP 访问日志：方法、URI、状态码、耗时；MDC traceId 供全链路日志关联。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessLoggingFilter.class);

    private static final String TRACE_ID = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        MDC.put(TRACE_ID, traceId);
        long start = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            String uri = request.getRequestURI();
            String q = request.getQueryString();
            if (q != null) {
                uri = uri + "?" + q;
            }
            int status = response.getStatus();
            boolean health = "/api/health".equals(request.getRequestURI());
            if (health && status < 400) {
                log.debug("{} {} {} {}ms", request.getMethod(), uri, status, ms);
            } else if (status >= 500) {
                log.error("{} {} {} {}ms", request.getMethod(), uri, status, ms);
            } else if (status >= 400) {
                log.warn("{} {} {} {}ms", request.getMethod(), uri, status, ms);
            } else {
                log.info("{} {} {} {}ms", request.getMethod(), uri, status, ms);
            }
            MDC.remove(TRACE_ID);
        }
    }
}
