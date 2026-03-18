package com.pm.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_route")
public class SysRoute extends BaseEntity {

    private Long parentId;
    /**
     * constant / async
     */
    private String routeType;
    private Integer sort;

    private String path;
    private String name;
    /**
     * 前端组件标识：如 Layout / views/dashboard/Dashboard
     */
    private String component;
    private String redirect;
    /**
     * JSON 字符串，原样存 meta
     */
    private String metaJson;
}

