package com.pjb.mapper;

import com.pjb.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lsj
 * @date 2019.7
 */
@Component
public interface SysPermissionMapper {
    //根据角色ID查询角色对应的权限信息
    List<SysPermission> findPermissionByRoleId(@Param("roleId") Integer roleId);
}
