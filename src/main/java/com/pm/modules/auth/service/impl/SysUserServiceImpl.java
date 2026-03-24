package com.pm.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.auth.entity.SysRole;
import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.entity.SysUserRole;
import com.pm.modules.auth.mapper.SysRoleMapper;
import com.pm.modules.auth.mapper.SysUserMapper;
import com.pm.modules.auth.mapper.SysUserRoleMapper;
import com.pm.modules.auth.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** 用户服务实现：按用户名查用户、查用户角色编码 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;

    public SysUserServiceImpl(SysUserRoleMapper sysUserRoleMapper, SysRoleMapper sysRoleMapper) {
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMapper = sysRoleMapper;
    }

    /** 按登录名查询唯一用户 */
    @Override
    public SysUser getByUsername(String username) {
        return lambdaQuery().eq(SysUser::getUsername, username).one();
    }

    /** 查询用户关联的角色 code 列表（小写） */
    @Override
    public List<String> getDbRoleCodeListByUsername(String username) {
        SysUser user = getByUsername(username);
        if (user == null) {
            return List.of();
        }
        List<SysUserRole> urs = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, user.getId())
                        .eq(SysUserRole::getDeleted, 0));
        if (urs.isEmpty()) {
            return List.of();
        }
        List<Long> roleIds = urs.stream().map(SysUserRole::getRoleId).distinct().collect(Collectors.toList());
        if (roleIds.isEmpty()) {
            return List.of();
        }
        List<SysRole> roles = sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .in(SysRole::getId, roleIds)
                        .eq(SysRole::getDeleted, 0));
        List<String> codes = new ArrayList<>();
        for (SysRole r : roles) {
            if (r != null && r.getRoleCode() != null) {
                codes.add(r.getRoleCode().trim().toLowerCase());
            }
        }
        return codes;
    }
}

