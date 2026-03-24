package com.pm.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/** 启动完成后打印健康检查与登录接口地址 */
@Component
public class StartupLogger implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final Environment environment;

    public StartupLogger(Environment environment) {
        this.environment = environment;
    }

    /** 根据 server.port 与 context-path 输出 /api/getHealth、/api/auth/createLoginToken 的完整 URL */
    @Override
    public void run(String... args) {
        String port = environment.getProperty("server.port", "8080");
        String ctx = environment.getProperty("server.servlet.context-path", "");
        String base = "http://localhost:" + port + (ctx.isEmpty() ? "" : ctx);
        log.info("应用已启动 | 健康检查: {}/api/getHealth | 登录: {}/api/auth/createLoginToken", base, base);
    }
}

