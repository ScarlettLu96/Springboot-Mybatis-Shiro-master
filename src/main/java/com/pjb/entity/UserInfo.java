package com.pjb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lsj
 * @date 2019.7
 * 用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    Integer id;//用户id
    String username;//帐号
    String worknumber;//工号（暂代表手机号）
    String password;
    String salt;
    int state;
    /**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){

        return this.username+this.salt;
    }
}
