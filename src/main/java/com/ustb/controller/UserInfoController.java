package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.UserInfo;
import com.ustb.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/")
@CrossOrigin
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("list")
    @ResponseBody
    public Result list() {
        List<UserInfo> list = userInfoService.findList();
        return new Result(100, list);
    }

    @PostMapping("update-password")
    @ResponseBody
    public Result updatePassword(@RequestBody Map<String, Object> params) {
        Long currentUserId = Long.valueOf(params.get("currentUserId").toString());
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");

        int code = userInfoService.updatePassword(currentUserId, oldPassword, newPassword);
        if (code == 100) {
            return new Result(100, "密码修改成功");
        } else if (code == 102) {
            return new Result(101, "旧密码错误");
        } else {
            return new Result(101, "密码修改失败");
        }
    }
}
