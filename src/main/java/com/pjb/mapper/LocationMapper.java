package com.pjb.mapper;

import com.pjb.entity.Location;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface LocationMapper {
    Location selectById(@Param("id") Integer id);
}
