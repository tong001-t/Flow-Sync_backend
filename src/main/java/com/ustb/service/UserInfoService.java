package com.ustb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ustb.dao.UserInfoMapper;
import com.ustb.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserInfo> findList() {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id", "username", "real_name", "role", "phone", "email", "create_time");
        return userInfoMapper.selectList(wrapper);
    }

    /**
     * 登录验证（BCrypt 密码比对）
     * 兼容旧明文密码：首次登录时自动升级为 BCrypt 加密
     */
    public UserInfo login(String username, String rawPassword) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        UserInfo user = userInfoMapper.selectOne(wrapper);

        if (user == null) {
            return null;
        }

        String storedPassword = user.getPassword();

        // BCrypt 密文以 $2a$ 开头
        if (storedPassword.startsWith("$2a$")) {
            // BCrypt 比对
            if (passwordEncoder.matches(rawPassword, storedPassword)) {
                user.setPassword(null); // 不返回密码
                return user;
            }
            return null;
        }

        // 旧明文密码比对（兼容历史数据）
        if (storedPassword.equals(rawPassword)) {
            // 自动升级为 BCrypt 加密
            String encoded = passwordEncoder.encode(rawPassword);
            user.setPassword(encoded);
            userInfoMapper.updateById(user);

            user.setPassword(null);
            return user;
        }

        return null;
    }

    public UserInfo findById(Long id) {
        return userInfoMapper.selectById(id);
    }

    /**
     * 修改密码（BCrypt）
     */
    public int updatePassword(Long userId, String oldPassword, String newPassword) {
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            return 101;
        }

        String storedPassword = user.getPassword();

        // BCrypt 密文比对
        if (storedPassword.startsWith("$2a$")) {
            if (!passwordEncoder.matches(oldPassword, storedPassword)) {
                return 102; // 旧密码错误
            }
        } else {
            // 兼容旧明文密码
            if (!storedPassword.equals(oldPassword)) {
                return 102;
            }
        }

        // 新密码 BCrypt 加密存储
        user.setPassword(passwordEncoder.encode(newPassword));
        return userInfoMapper.updateById(user) > 0 ? 100 : 101;
    }

    /**
     * 批量加密所有明文密码（一次性迁移用）
     */
    public int migratePlainPasswords() {
        List<UserInfo> allUsers = userInfoMapper.selectList(null);
        int count = 0;
        for (UserInfo user : allUsers) {
            String pwd = user.getPassword();
            if (pwd != null && !pwd.startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(pwd));
                userInfoMapper.updateById(user);
                count++;
            }
        }
        return count;
    }
}
