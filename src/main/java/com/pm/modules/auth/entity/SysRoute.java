package com.pm.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_route")
public class SysRoute extends BaseEntity {

    /** 父级 ID，0 为顶级 */
    private Long parentId;
    /** 路由类型：constant / async */
    private String routeType;
    /** 排序，升序 */
    private Integer sort;
    /** 路由 path */
    private String path;
    /** 路由 name */
    private String name;
    /** 前端组件标识（如 Layout、views/dashboard/index） */
    private String component;
    /** 重定向地址 */
    private String redirect;
    /** meta 的 JSON 字符串 */
    private String metaJson;
}

