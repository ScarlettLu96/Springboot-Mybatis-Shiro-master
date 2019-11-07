package com.pjb.service.impl;

import com.pjb.entity.Location;
import com.pjb.entity.UserInfo;
import com.pjb.mapper.LocationMapper;
import com.pjb.mapper.UserInfoMapper;
import com.pjb.result.ResultInfo;
import com.pjb.service.UserInfoService;
import com.pjb.util.UserRegisteAndLogin;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lsj
 * @date 2019.7
 */
@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    LocationMapper locationMapper;

    @Override
    public UserInfo findByUsername(String username) {
        return userInfoMapper.findByUsername(username);
    }

    @Override
    public String findSaltByUsername(String username){
        return userInfoMapper.findSaltByUsername(username);
    }

    @Override
    public String addUser(String username,String password,String worknumber,String salt,int state){
        return userInfoMapper.addUser(username,password,worknumber,salt,state);
    }

    @Override
    public String verifyUser(String username){
        UserInfo user = userInfoMapper.findByUsername(username);
        // 这里代表如果通过用户名没有查询到用户信息，即代表未重名返回SUCCESS，否则返回ERROR代表以重名
        if(user == null){
            return "SUCCESS";
        }else{
            return "ERROR";
        }
    }

    @Override
    public String retrievePassword(String password,String username){

        String[] saltAndCiphertext = UserRegisteAndLogin.encryptPassword(password,username);
        return userInfoMapper.retrievePassword(username,saltAndCiphertext[0],saltAndCiphertext[1]);
    }

    /**
     * 用于验证用户名密码是否正确
     * @param username
     * @param password
     * @return
     */
    @Override
    public String findPasswordByUsername(String username,String password){
        UserInfo userInfo=userInfoMapper.findPasswordByUsername(username);
        String ciphertext = new Md5Hash(password,username+userInfo.getSalt(),2).toString();
        if(ciphertext.equals(userInfo.getPassword())){
            return "Have permission to modify!";
        }
        else{
            return "Please check the password";
        }
    }

    @Override
    public Location gis(Integer id){
        Location location =locationMapper.selectById(id);
        return location;
    }

}
