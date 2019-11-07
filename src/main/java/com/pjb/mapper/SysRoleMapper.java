package com.pjb.mapper;

import com.pjb.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lsj
 * @date 2019.7
 */
@Component
public interface SysRoleMapper {
    //通过username查找用户角色信息
    List<SysRole> findRoleByUsername(@Param("username") String username);
}
