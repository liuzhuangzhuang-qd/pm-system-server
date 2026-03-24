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
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.UUID;

/** HTTP 访问日志：方法、URI、状态码、耗时；MDC traceId 供全链路日志关联；请求 body/headers 脱敏 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessLoggingFilter.class);

    private static final String TRACE_ID = "traceId";
    private static final int MAX_BODY_LOG_SIZE = 8 * 1024;
    private static final int MAX_HEADER_LOG_SIZE = 30;

    /** 注入 traceId、包装请求/响应、记录访问日志与可选请求详情 */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        MDC.put(TRACE_ID, traceId);
        long start = System.nanoTime();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            String uri = request.getRequestURI();
            String q = request.getQueryString();
            if (q != null) {
                uri = uri + "?" + q;
            }
            int status = responseWrapper.getStatus();
            boolean health = "/api/getHealth".equals(request.getRequestURI());
            if (health && status < 400) {
                log.debug("{} {} {} {}ms", request.getMethod(), uri, status, ms);
            } else if (status >= 500) {
                log.error("{} {} {} {}ms", request.getMethod(), uri, status, ms);
            } else if (status >= 400) {
                log.warn("{} {} {} {}ms", request.getMethod(), uri, status, ms);
            } else {
                log.info("{} {} {} {}ms", request.getMethod(), uri, status, ms);
            }

            // 记录请求详细信息：query/body/headers（脱敏 password；不打印 Authorization/Cookie）
            boolean logDetails = !health && (status >= 400 || request.getRequestURI().startsWith("/api/auth") || !"GET".equalsIgnoreCase(request.getMethod()));
            if (logDetails) {
                String headers = safeHeadersToString(request);
                String body = safeRequestBody(requestWrapper);
                if (body == null) {
                    log.info("REQ {} {} headers={}", request.getMethod(), request.getRequestURI(), headers);
                } else {
                    log.info("REQ {} {} headers={} body={}", request.getMethod(), request.getRequestURI(), headers, body);
                }
            }

            responseWrapper.copyBodyToResponse();
            MDC.remove(TRACE_ID);
        }
    }

    /** 将请求头转为字符串（排除 Authorization、Cookie），单值截断 */
    private String safeHeadersToString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        Enumeration<String> names = request.getHeaderNames();
        while (names != null && names.hasMoreElements()) {
            String name = names.nextElement();
            if (name == null) continue;
            // 避免敏感信息进入日志
            if ("authorization".equalsIgnoreCase(name) || "cookie".equalsIgnoreCase(name)) {
                continue;
            }
            String value = request.getHeader(name);
            if (value == null) value = "";
            sb.append(name).append("=").append(trimTo(value, 200)).append("; ");
            count++;
            if (count >= MAX_HEADER_LOG_SIZE) break;
        }
        return sb.toString();
    }

    /** 读取缓存 body 并做 password 等字段脱敏 */
    private String safeRequestBody(ContentCachingRequestWrapper requestWrapper) {
        byte[] buf = requestWrapper.getContentAsByteArray();
        if (buf == null || buf.length == 0) {
            return null;
        }

        int len = Math.min(buf.length, MAX_BODY_LOG_SIZE);
        String contentType = requestWrapper.getContentType();
        String body = new String(buf, 0, len, getCharsetOrUtf8(requestWrapper));
        body = body.trim();
        if (body.isEmpty()) return null;

        return maskSensitive(body, contentType);
    }

    /** 对 JSON 或 form 中的 password/newPassword/oldPassword/pwd 做脱敏 */
    private static String maskSensitive(String text, String contentType) {
        String out = text;
        out = out.replaceAll("(?i)(\"password\"\\s*:\\s*\")([^\"]*)(\")", "$1***$3");
        out = out.replaceAll("(?i)(\"newPassword\"\\s*:\\s*\")([^\"]*)(\")", "$1***$3");
        out = out.replaceAll("(?i)(\"oldPassword\"\\s*:\\s*\")([^\"]*)(\")", "$1***$3");
        out = out.replaceAll("(?i)(\"pwd\"\\s*:\\s*\")([^\"]*)(\")", "$1***$3");
        // 如果是 x-www-form-urlencoded，仍可能是 password=xxx 的形式
        out = out.replaceAll("(?i)(password=)([^&\\s]+)", "$1***");
        out = out.replaceAll("(?i)(pwd=)([^&\\s]+)", "$1***");
        return out;
    }

    private static String trimTo(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...";
    }

    private static java.nio.charset.Charset getCharsetOrUtf8(ContentCachingRequestWrapper wrapper) {
        String enc = wrapper.getCharacterEncoding();
        if (enc == null || enc.isBlank()) return StandardCharsets.UTF_8;
        try {
            return java.nio.charset.Charset.forName(enc);
        } catch (Exception e) {
            return StandardCharsets.UTF_8;
        }
    }
}
