package com.pjb.service;

import com.pjb.entity.Location;
import com.pjb.entity.UserInfo;
import com.pjb.result.ResultInfo;

/**
 * @author jinbin
 * @date 2017-12-01 21:22
 */
public interface UserInfoService {
    /**通过username查找用户信息;*/
    UserInfo findByUsername(String username);
    String findSaltByUsername(String username);
    String addUser(String username,String password,String worknumber,String salt,int state);
    String verifyUser(String username);//查找用户名是否存在
    String retrievePassword(String password,String username);//忘记密码
    String findPasswordByUsername(String username,String password);

    Location gis(Integer id);
}
