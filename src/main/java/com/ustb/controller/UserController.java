package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.User;
import com.ustb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("list")
    @ResponseBody
    public List<User> list() {
        return userService.listUsers();
    }

    @PostMapping("update-password")
    @ResponseBody
    public Result updatePassword(@RequestBody Map<String, Object> body) {
        Long userId = body.get("currentUserId") instanceof Number
                ? ((Number) body.get("currentUserId")).longValue() : null;
        String oldPassword = (String) body.get("oldPassword");
        String newPassword = (String) body.get("newPassword");

        if (userId == null) {
            return new Result(101, "用户未登录");
        }

        boolean success = userService.updatePassword(userId, oldPassword, newPassword);
        if (success) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "密码修改成功");
            return new Result(100, "密码修改成功", result);
        }
        return new Result(101, "原密码错误");
    }
}
