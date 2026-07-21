package com.ustb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;
    private String phone;
    private String email;
    private Date createTime;
}
