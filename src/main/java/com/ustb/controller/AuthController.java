package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.User;
import com.ustb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth/")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = userService.login(username, password);
        if (user != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("realName", user.getRealName());
            userData.put("role", user.getRole());
            userData.put("phone", user.getPhone());
            userData.put("email", user.getEmail());

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("data", userData);
            return new Result(100, "登录成功", userData);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "用户名或密码错误");
        return new Result(101, "用户名或密码错误");
    }
}
