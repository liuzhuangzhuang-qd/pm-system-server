package com.pm.common.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 健康检查接口，用于存活探测 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /** 返回 OK 表示服务正常 */
    @GetMapping("/getHealth")
    public String getHealth() {
        return "OK";
    }
}

