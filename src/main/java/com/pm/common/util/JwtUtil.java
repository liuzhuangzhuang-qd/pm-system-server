package com.pm.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${pm.security.jwt-secret}")
    private String secret;

    @Value("${pm.security.jwt-expire-seconds}")
    private long expireSeconds;

    /**
     * HS256 需要至少 256 位密钥。
     * 优先按 Base64URL 解码（推荐），失败再按 Base64，仍不满足则对明文做 SHA-256。
     */
    private Key getKey() {
        byte[] keyBytes;
        try {
            String s = secret == null ? "" : secret.trim();
            // 1) Base64URL 尝试（支持 - 和 _）
            byte[] urlDecoded = Decoders.BASE64URL.decode(s);
            if (urlDecoded.length >= 32) {
                keyBytes = urlDecoded;
            } else {
                // 2) 标准 Base64 再试
                byte[] stdDecoded = Decoders.BASE64.decode(s);
                keyBytes = (stdDecoded.length >= 32) ? stdDecoded : sha256Utf8(s);
            }
        } catch (DecodingException | IllegalArgumentException e) {
            // 3) 明文密钥走 SHA-256
            keyBytes = sha256Utf8(secret);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static byte[] sha256Utf8(String text) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + expireSeconds * 1000);
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

