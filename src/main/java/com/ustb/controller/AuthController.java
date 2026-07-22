package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.UserInfo;
import com.ustb.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth/")
@CrossOrigin
public class AuthController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        // BCrypt 密码验证 + 旧明文自动升级
        UserInfo user = userInfoService.login(username, password);
        if (user == null) {
            return new Result(101, "用户名或密码错误");
        }
        return new Result(100, user);
    }
}
