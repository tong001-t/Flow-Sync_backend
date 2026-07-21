package com.ustb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ustb.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
