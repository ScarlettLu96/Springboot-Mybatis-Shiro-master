package com.pjb.mapper;

import com.pjb.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author lsj
 * @date 2019.7
 */
@Component
public interface UserInfoMapper {
    //通过username查找用户信息
    UserInfo findByUsername(@Param("username") String username);

    String findSaltByUsername(@Param("username") String username);

    String addUser(@Param("username") String username,
                   @Param("password")String password,@Param("worknumber")String worknumber,
                   @Param("salt")String salt,@Param("state")int state);

    String retrievePassword(@Param("username") String username,
                            @Param("salt") String salt,
                            @Param("password") String password);

    UserInfo findPasswordByUsername(@Param("username") String username);
}
