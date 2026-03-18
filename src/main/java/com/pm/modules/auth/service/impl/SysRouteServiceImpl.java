package com.pm.modules.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.modules.auth.entity.SysRoute;
import com.pm.modules.auth.mapper.SysRouteMapper;
import com.pm.modules.auth.service.SysRouteService;
import com.pm.modules.auth.service.SysUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysRouteServiceImpl extends ServiceImpl<SysRouteMapper, SysRoute> implements SysRouteService {

    /** 库表 role_code -> 前端 router meta.roles */
    private static final Map<String, String> DB_ROLE_TO_FRONT = Map.of(
            "admin", "ADMIN",
            "manager", "PM",
            "dev", "DEV",
            "tester", "QA"
    );

    private final ObjectMapper objectMapper;
    private final SysUserService sysUserService;

    public SysRouteServiceImpl(ObjectMapper objectMapper, SysUserService sysUserService) {
        this.objectMapper = objectMapper;
        this.sysUserService = sysUserService;
    }

    @Override
    public Map<String, Object> getFrontRoutes() {
        List<SysRoute> routes = lambdaQuery()
                .eq(SysRoute::getDeleted, 0)
                .orderByAsc(SysRoute::getRouteType)
                .orderByAsc(SysRoute::getSort)
                .orderByAsc(SysRoute::getId)
                .list();

        List<Map<String, Object>> constant = buildTree(toFrontNodes(filterByType(routes, "constant")));
        List<Map<String, Object>> asyncFull = buildTree(toFrontNodes(filterByType(routes, "async")));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : null;
        boolean superAdmin = false;
        Set<String> frontRoles = new HashSet<>();
        if (username != null && !"anonymousUser".equals(username)) {
            List<String> dbCodes = sysUserService.listDbRoleCodesByUsername(username);
            for (String code : dbCodes) {
                if ("admin".equalsIgnoreCase(code)) {
                    superAdmin = true;
                    break;
                }
                String fr = DB_ROLE_TO_FRONT.get(code.toLowerCase());
                if (fr != null) {
                    frontRoles.add(fr);
                }
            }
        }

        List<Map<String, Object>> async = superAdmin ? asyncFull : filterAsyncByRoles(asyncFull, frontRoles);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("constantRoutes", constant);
        result.put("asyncRoutes", async);
        return result;
    }

    /**
     * 仅保留：当前节点 meta.roles 与用户角色有交集，且子树中至少有一个可访问节点（Layout 无子则整段剔除）
     */
    private static List<Map<String, Object>> filterAsyncByRoles(List<Map<String, Object>> roots, Set<String> userRoles) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map<String, Object> root : roots) {
            Map<String, Object> kept = filterNode(root, userRoles);
            if (kept != null) {
                out.add(kept);
            }
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> filterNode(Map<String, Object> node, Set<String> userRoles) {
        if (!metaRoleAllows(node, userRoles)) {
            return null;
        }
        Object chObj = node.get("children");
        if (!(chObj instanceof List)) {
            return deepCopyNode(node);
        }
        List<Map<String, Object>> children = (List<Map<String, Object>>) chObj;
        if (children.isEmpty()) {
            return deepCopyNode(node);
        }
        List<Map<String, Object>> kept = new ArrayList<>();
        for (Map<String, Object> c : children) {
            Map<String, Object> fc = filterNode(c, userRoles);
            if (fc != null) {
                kept.add(fc);
            }
        }
        if (kept.isEmpty()) {
            return null;
        }
        Map<String, Object> copy = deepCopyNode(node);
        copy.put("children", kept);
        return copy;
    }

    @SuppressWarnings("unchecked")
    private static boolean metaRoleAllows(Map<String, Object> node, Set<String> userRoles) {
        Object metaObj = node.get("meta");
        if (!(metaObj instanceof Map)) {
            return true;
        }
        Map<String, Object> meta = (Map<String, Object>) metaObj;
        Object rolesObj = meta.get("roles");
        if (rolesObj == null) {
            return true;
        }
        if (!(rolesObj instanceof List)) {
            return true;
        }
        List<?> req = (List<?>) rolesObj;
        if (req.isEmpty()) {
            return true;
        }
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }
        for (Object r : req) {
            if (r != null && userRoles.contains(String.valueOf(r))) {
                return true;
            }
        }
        return false;
    }

    private static Map<String, Object> deepCopyNode(Map<String, Object> node) {
        Map<String, Object> copy = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : node.entrySet()) {
            if ("children".equals(e.getKey())) {
                continue;
            }
            Object v = e.getValue();
            if (v instanceof Map) {
                copy.put(e.getKey(), new LinkedHashMap<>((Map<String, Object>) v));
            } else {
                copy.put(e.getKey(), v);
            }
        }
        return copy;
    }

    private static List<SysRoute> filterByType(List<SysRoute> routes, String type) {
        List<SysRoute> out = new ArrayList<>();
        for (SysRoute r : routes) {
            if (type.equalsIgnoreCase(r.getRouteType())) {
                out.add(r);
            }
        }
        return out;
    }

    private List<Map<String, Object>> toFrontNodes(List<SysRoute> routes) {
        List<Map<String, Object>> out = new ArrayList<>(routes.size());
        for (SysRoute r : routes) {
            Map<String, Object> node = new LinkedHashMap<>();
            node.put("id", r.getId());
            node.put("parentId", r.getParentId());
            node.put("path", r.getPath());
            if (r.getName() != null) {
                node.put("name", r.getName());
            }
            if (r.getComponent() != null) {
                node.put("component", r.getComponent());
            }
            if (r.getRedirect() != null) {
                node.put("redirect", r.getRedirect());
            }
            node.put("meta", parseMeta(r.getMetaJson()));
            out.add(node);
        }
        return out;
    }

    private Map<String, Object> parseMeta(String metaJson) {
        if (metaJson == null || metaJson.isBlank()) {
            return new LinkedHashMap<>();
        }
        try {
            return objectMapper.readValue(metaJson, new TypeReference<LinkedHashMap<String, Object>>() {});
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    private static List<Map<String, Object>> buildTree(List<Map<String, Object>> flat) {
        Map<Long, Map<String, Object>> byId = new HashMap<>();
        for (Map<String, Object> n : flat) {
            Object idObj = n.get("id");
            if (idObj instanceof Number) {
                byId.put(((Number) idObj).longValue(), n);
            }
        }

        List<Map<String, Object>> roots = new ArrayList<>();
        for (Map<String, Object> n : flat) {
            Object pidObj = n.get("parentId");
            Long pid = (pidObj instanceof Number) ? ((Number) pidObj).longValue() : null;
            if (pid == null || pid == 0L || !byId.containsKey(pid)) {
                roots.add(stripDbFields(n));
                continue;
            }
            Map<String, Object> parent = byId.get(pid);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> children = (List<Map<String, Object>>) parent.computeIfAbsent("children", k -> new ArrayList<>());
            children.add(stripDbFields(n));
        }

        for (Map<String, Object> n : byId.values()) {
            stripDbFields(n);
        }
        return roots;
    }

    private static Map<String, Object> stripDbFields(Map<String, Object> node) {
        node.remove("id");
        node.remove("parentId");
        return node;
    }
}
