package com.ustb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ustb.dao.UserInfoMapper;
import com.ustb.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    public List<UserInfo> findList() {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id", "username", "real_name", "role", "phone", "email", "create_time");
        return userInfoMapper.selectList(wrapper);
    }

    public UserInfo findByUsername(String username) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userInfoMapper.selectOne(wrapper);
    }

    public UserInfo findById(Long id) {
        return userInfoMapper.selectById(id);
    }

    public int updatePassword(Long userId, String oldPassword, String newPassword) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            return 101;
        }
        if (!user.getPassword().equals(oldPassword)) {
            return 102;
        }
        user.setPassword(newPassword);
        return userInfoMapper.updateById(user) > 0 ? 100 : 101;
    }
}
