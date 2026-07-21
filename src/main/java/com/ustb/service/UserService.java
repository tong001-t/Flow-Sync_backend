package com.ustb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ustb.dao.UserMapper;
import com.ustb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", password);
        return userMapper.selectOne(wrapper);
    }

    public List<User> listUsers() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id", "username", "real_name", "role", "phone", "email", "create_time");
        return userMapper.selectList(wrapper);
    }

    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            return userMapper.updateById(user) > 0;
        }
        return false;
    }
}
